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
package org.camunda.bpm.engine.rest;

import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.identity.GroupDto;
import org.camunda.bpm.engine.rest.sub.identity.GroupResource;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * @author Daniel Meyer
 *
 */
@Path("/{engineName : (engine/[^/]+/)?}group")
public interface GroupRestService {

  String PATH = "/group";

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  GroupDto getGroup(@PathParam("id") String id, @Context UriInfo context);

  @DELETE
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  void deleteGroup(@PathParam("id") String id);

  @PUT
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  void updateGroup(@PathParam("id") String id, GroupDto Group);

  @OPTIONS
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  ResourceOptionsDto availableOperationsForGroupResource(@PathParam("id") String id, @Context UriInfo context);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<GroupDto> queryGroups(@Context UriInfo uriInfo,
      @QueryParam("firstResult") Integer firstResult, @QueryParam("maxResults") Integer maxResults);

  @GET
  @Path("/count")
  @Produces(MediaType.APPLICATION_JSON)
  CountResultDto getGroupCount(@Context UriInfo uriInfo);

  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  void createGroup(GroupDto GroupDto);

  @OPTIONS
  @Produces(MediaType.APPLICATION_JSON)
  ResourceOptionsDto availableOperations(@Context UriInfo context);
}
