/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.GroupQuery;
import org.camunda.bpm.engine.rest.GroupRestService;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.identity.GroupDto;
import org.camunda.bpm.engine.rest.dto.identity.GroupQueryDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.sub.identity.GroupResource;
import org.camunda.bpm.engine.rest.sub.identity.impl.AbstractIdentityResource;
import org.camunda.bpm.engine.rest.sub.identity.impl.GroupResourceImpl;
import org.camunda.bpm.engine.rest.util.PathUtil;
import org.camunda.bpm.engine.rest.util.ProvidersUtil;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;
import java.net.URI;
import java.util.List;

import static org.camunda.bpm.engine.authorization.Authorization.ANY;
import static org.camunda.bpm.engine.authorization.Permissions.CREATE;
import static org.camunda.bpm.engine.authorization.Permissions.DELETE;
import static org.camunda.bpm.engine.authorization.Permissions.UPDATE;
import static org.camunda.bpm.engine.authorization.Resources.GROUP;

/**
 * @author Daniel Meyer
 *
 */
public class GroupRestServiceImpl extends AbstractIdentityResource implements GroupRestService {

  @PathParam("engineName")
  public String param;

  @Context
  Providers providers;
  private String rootResourcePath;

  public GroupRestServiceImpl() {
    super(null , GROUP, null, null);
  }

  public GroupRestServiceImpl(String engineName, final ObjectMapper objectMapper) {
    super(engineName, GROUP, ANY, objectMapper);
  }

//  public GroupResourceImpl(String processEngineName, String groupId, String rootResourcePath, ObjectMapper objectMapper) {
//    super(processEngineName, GROUP, groupId, objectMapper);
//    this.rootResourcePath = rootResourcePath;
//  }

  public GroupDto getGroup(String id, UriInfo context) {
    resourceId = id;

    Group dbGroup = findGroupObject();
    if(dbGroup == null) {
      throw new InvalidRequestException(Status.NOT_FOUND, "Group with id " + resourceId + " does not exist");
    }

    GroupDto group = GroupDto.fromGroup(dbGroup);

    return group;
  }

  public ResourceOptionsDto availableOperationsForGroupResource(String id, UriInfo context) {
    resourceId = id;
    ResourceOptionsDto dto = new ResourceOptionsDto();

    // add links if operations are authorized
    URI uri = context.getBaseUriBuilder()
      .path(rootResourcePath)
      .path(GroupRestService.PATH)
      .path(resourceId)
      .build();

    dto.addReflexiveLink(uri, HttpMethod.GET, "self");
    if(!identityService.isReadOnly() && isAuthorized(DELETE)) {
      dto.addReflexiveLink(uri, HttpMethod.DELETE, "delete");
    }
    if(!identityService.isReadOnly() && isAuthorized(UPDATE)) {
      dto.addReflexiveLink(uri, HttpMethod.PUT, "update");
    }

    return dto;
  }


  public void updateGroup(String id, GroupDto group) {
    resourceId = id;
    ensureNotReadOnly();

    Group dbGroup = findGroupObject();
    if(dbGroup == null) {
      throw new InvalidRequestException(Status.NOT_FOUND, "Group with id " + resourceId + " does not exist");
    }

    group.update(dbGroup);

    identityService.saveGroup(dbGroup);
  }


  public void deleteGroup(String id) {
    resourceId = id;
    ensureNotReadOnly();
    identityService.deleteGroup(resourceId);
  }

  protected Group findGroupObject() {
    try {
      return identityService.createGroupQuery()
        .groupId(resourceId)
        .singleResult();
    } catch(ProcessEngineException e) {
      throw new InvalidRequestException(Status.INTERNAL_SERVER_ERROR, "Exception while performing group query: "+e.getMessage());
    }
  }

  public List<GroupDto> queryGroups(UriInfo uriInfo, Integer firstResult, Integer maxResults) {
    objectMapper = ProvidersUtil.resolveFromContext(providers, ObjectMapper.class, MediaType.APPLICATION_JSON_TYPE, this.getClass());
    GroupQueryDto queryDto = new GroupQueryDto(getObjectMapper(), uriInfo.getQueryParameters());
    return queryGroups(queryDto, firstResult, maxResults);
  }

  public List<GroupDto> queryGroups(GroupQueryDto queryDto, Integer firstResult, Integer maxResults) {
    objectMapper = ProvidersUtil.resolveFromContext(providers, ObjectMapper.class, MediaType.APPLICATION_JSON_TYPE, this.getClass());
    queryDto.setObjectMapper(getObjectMapper());
    GroupQuery query = queryDto.toQuery(getProcessEngine());

    List<Group> resultList;
    if(firstResult != null || maxResults != null) {
      resultList = executePaginatedQuery(query, firstResult, maxResults);
    } else {
      resultList = query.list();
    }

    return GroupDto.fromGroupList(resultList);
  }

  public CountResultDto getGroupCount(UriInfo uriInfo) {
    objectMapper = ProvidersUtil.resolveFromContext(providers, ObjectMapper.class, MediaType.APPLICATION_JSON_TYPE, this.getClass());

    GroupQueryDto queryDto = new GroupQueryDto(getObjectMapper(), uriInfo.getQueryParameters());
    return getGroupCount(queryDto);
  }

  protected CountResultDto getGroupCount(GroupQueryDto queryDto) {
    GroupQuery query = queryDto.toQuery(getProcessEngine());
    long count = query.count();
    return new CountResultDto(count);
  }


  public ResourceOptionsDto availableOperations(UriInfo context) {
    final IdentityService identityService = getIdentityService();

    UriBuilder baseUriBuilder = context.getBaseUriBuilder()
        .path(relativeRootResourcePath)
        .path(GroupRestService.PATH);

    ResourceOptionsDto resourceOptionsDto = new ResourceOptionsDto();

    // GET /
    URI baseUri = baseUriBuilder.build();
    resourceOptionsDto.addReflexiveLink(baseUri, HttpMethod.GET, "list");

    // GET /count
    URI countUri = baseUriBuilder.clone().path("/count").build();
    resourceOptionsDto.addReflexiveLink(countUri, HttpMethod.GET, "count");

    // POST /create
    if(!identityService.isReadOnly() && isAuthorized(CREATE)) {
      URI createUri = baseUriBuilder.clone().path("/create").build();
      resourceOptionsDto.addReflexiveLink(createUri, HttpMethod.POST, "create");
    }

    return resourceOptionsDto;
  }

  // utility methods //////////////////////////////////////

  protected List<Group> executePaginatedQuery(GroupQuery query, Integer firstResult, Integer maxResults) {
    if (firstResult == null) {
      firstResult = 0;
    }
    if (maxResults == null) {
      maxResults = Integer.MAX_VALUE;
    }
    return query.listPage(firstResult, maxResults);
  }

  protected IdentityService getIdentityService() {
    return getProcessEngine().getIdentityService();
  }


  public void createGroup(GroupDto groupDto) {
    final IdentityService identityService = getIdentityService();

    if(identityService.isReadOnly()) {
      throw new InvalidRequestException(Status.FORBIDDEN, "Identity service implementation is read-only.");
    }

    Group newGroup = identityService.newGroup(groupDto.getId());
    groupDto.update(newGroup);
    identityService.saveGroup(newGroup);

  }
}
