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

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.camunda.bpm.engine.rest.AuthorizationRestService;
import org.camunda.bpm.engine.rest.BatchRestService;
import org.camunda.bpm.engine.rest.CaseDefinitionRestService;
import org.camunda.bpm.engine.rest.CaseExecutionRestService;
import org.camunda.bpm.engine.rest.CaseInstanceRestService;
import org.camunda.bpm.engine.rest.DecisionDefinitionRestService;
import org.camunda.bpm.engine.rest.DecisionRequirementsDefinitionRestService;
import org.camunda.bpm.engine.rest.DeploymentRestService;
import org.camunda.bpm.engine.rest.ExecutionRestService;
import org.camunda.bpm.engine.rest.ExternalTaskRestService;
import org.camunda.bpm.engine.rest.FilterRestService;
import org.camunda.bpm.engine.rest.GroupRestService;
import org.camunda.bpm.engine.rest.IdentityRestService;
import org.camunda.bpm.engine.rest.IncidentRestService;
import org.camunda.bpm.engine.rest.JobDefinitionRestService;
import org.camunda.bpm.engine.rest.JobRestService;
import org.camunda.bpm.engine.rest.MessageRestService;
import org.camunda.bpm.engine.rest.MetricsRestService;
import org.camunda.bpm.engine.rest.MigrationRestService;
import org.camunda.bpm.engine.rest.ProcessDefinitionRestService;
import org.camunda.bpm.engine.rest.ProcessInstanceRestService;
import org.camunda.bpm.engine.rest.TaskRestService;
import org.camunda.bpm.engine.rest.TenantRestService;
import org.camunda.bpm.engine.rest.UserRestService;
import org.camunda.bpm.engine.rest.VariableInstanceRestService;
import org.camunda.bpm.engine.rest.dto.ProcessEngineDto;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.rest.spi.ProcessEngineProvider;
import org.camunda.bpm.engine.rest.sub.authorization.AuthorizationResource;
import org.camunda.bpm.engine.rest.sub.identity.*;
import org.camunda.bpm.engine.rest.sub.runtime.FilterResource;

@Path(NamedProcessEngineRestServiceImpl.PATH)
public class NamedProcessEngineRestServiceImpl extends AbstractProcessEngineRestServiceImpl {

  public static final String PATH = "/engine";

  @Override
  @Path("/{name}" + ProcessDefinitionRestService.PATH)
  public ProcessDefinitionRestService getProcessDefinitionService(@PathParam("name") String engineName) {
    return super.getProcessDefinitionService(engineName);
  }

  @Override
  @Path("/{name}" + ProcessInstanceRestService.PATH)
  public ProcessInstanceRestService getProcessInstanceService(@PathParam("name") String engineName) {
    return super.getProcessInstanceService(engineName);
  }

  @Override
  @Path("/{name}" + ExecutionRestService.PATH)
  public ExecutionRestService getExecutionService(@PathParam("name") String engineName) {
    return super.getExecutionService(engineName);
  }

  @Override
  @Path("/{name}" + TaskRestService.PATH)
  public TaskRestService getTaskRestService(@PathParam("name") String engineName) {
    return super.getTaskRestService(engineName);
  }

  @Override
  @Path("/{name}" + IdentityRestService.PATH)
  public IdentityRestService getIdentityRestService(@PathParam("name") String engineName) {
    return super.getIdentityRestService(engineName);
  }

  @Override
  @Path("/{name}" + MessageRestService.PATH)
  public MessageRestService getMessageRestService(@PathParam("name") String engineName) {
    return super.getMessageRestService(engineName);
  }

  @Override
  @Path("/{name}" + VariableInstanceRestService.PATH)
  public VariableInstanceRestService getVariableInstanceService(@PathParam("name") String engineName) {
    return super.getVariableInstanceService(engineName);
  }

  @Override
  @Path("/{name}" + JobDefinitionRestService.PATH)
  public JobDefinitionRestService getJobDefinitionRestService(@PathParam("name") String engineName) {
    return super.getJobDefinitionRestService(engineName);
  }

  @Override
  @Path("/{name}" + JobRestService.PATH)
  public JobRestService getJobRestService(@PathParam("name") String engineName) {
    return super.getJobRestService(engineName);
  }
//
//  @Override
//  @Path("/{name}" + GroupRestService.PATH)
//  public GroupRestService getGroupRestService(@PathParam("name") String engineName) {
//    return super.getGroupRestService(engineName);
//  }
//
//  @OPTIONS
//  @Path("/{name}" + GroupRestService.PATH)
//  @Produces(MediaType.APPLICATION_JSON)
//  public ResourceOptionsDto availableOperationsForGroupRestService(@PathParam("name") String engineName, @Context UriInfo context) {
//    return super.getGroupRestService(engineName).availableOperations(context);
//  }
//
//  @OPTIONS
//  @Path("/{name}" + GroupRestService.PATH + "/{id}")
//  @Produces(MediaType.APPLICATION_JSON)
//  public ResourceOptionsDto availableOperationsForGroupResource(@PathParam("name") String engineName,
//                                                                   @PathParam("id") String id,
//                                                                   @Context UriInfo context) {
//    return super.getGroupRestService(engineName).getGroup(id).availableOperations(context);
//  }
//
//  @OPTIONS
//  @Path("/{name}" + GroupRestService.PATH + "/{id}" + GroupMembersResource.PATH)
//  @Produces(MediaType.APPLICATION_JSON)
//  public ResourceOptionsDto availableOperationsForGroupMemberResource(@PathParam("name") String engineName,
//                                                                @PathParam("id") String id,
//                                                                @Context UriInfo context) {
//    return super.getGroupRestService(engineName).getGroup(id).getGroupMembersResource().availableOperations(context);
//  }

  @Override
  @Path("/{name}" + UserRestService.PATH)
  public UserRestService getUserRestService(@PathParam("name") String engineName) {
    return super.getUserRestService(engineName);
  }

  @OPTIONS
  @Path("/{name}" + UserRestService.PATH)
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForUserRestService(@PathParam("name") String engineName, @Context UriInfo context) {
    return super.getUserRestService(engineName).availableOperations(context);
  }

  @OPTIONS
  @Path("/{name}" + UserRestService.PATH + "/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForUserResource(@PathParam("name") String engineName, @PathParam("id") String id, @Context UriInfo context) {
    return super.getUserRestService(engineName).getUser(id).availableOperations(context);
  }

  @Override
  @Path("/{name}" + AuthorizationRestService.PATH)
  public AuthorizationRestService getAuthorizationRestService(@PathParam("name") String engineName) {
    return super.getAuthorizationRestService(engineName);
  }

  @OPTIONS
  @Path("/{name}" + AuthorizationRestService.PATH)
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForAuthorizationRestService(@PathParam("name") String engineName, @Context UriInfo context) {
    return super.getAuthorizationRestService(engineName).availableOperations(context);
  }

  @OPTIONS
  @Path("/{name}" + AuthorizationRestService.PATH + "/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForAuthorizationResource(@PathParam("name") String engineName,
                                                                           @PathParam("id") String id,
                                                                           @Context UriInfo context) {
    return super.getAuthorizationRestService(engineName).getAuthorization(id).availableOperations(context);
  }

  @Override
  @Path("/{name}" + IncidentRestService.PATH)
  public IncidentRestService getIncidentService(@PathParam("name") String engineName) {
    return super.getIncidentService(engineName);
  }

  @Override
  @Path("/{name}" + HistoryRestService.PATH)
  public HistoryRestService getHistoryRestService(@PathParam("name") String engineName) {
    return super.getHistoryRestService(engineName);
  }

  @Override
  @Path("/{name}" + DeploymentRestService.PATH)
  public DeploymentRestService getDeploymentRestService(@PathParam("name") String engineName) {
    return super.getDeploymentRestService(engineName);
  }

  @Override
  @Path("/{name}" + CaseDefinitionRestService.PATH)
  public CaseDefinitionRestService getCaseDefinitionRestService(@PathParam("name") String engineName) {
    return super.getCaseDefinitionRestService(engineName);
  }

  @Override
  @Path("/{name}" + CaseInstanceRestService.PATH)
  public CaseInstanceRestService getCaseInstanceRestService(@PathParam("name") String engineName) {
    return super.getCaseInstanceRestService(engineName);
  }

  @Override
  @Path("/{name}" + CaseExecutionRestService.PATH)
  public CaseExecutionRestService getCaseExecutionRestService(@PathParam("name") String engineName) {
    return super.getCaseExecutionRestService(engineName);
  }

  @Override
  @Path("/{name}" + FilterRestService.PATH)
  public FilterRestService getFilterRestService(@PathParam("name") String engineName) {
    return super.getFilterRestService(engineName);
  }

  @OPTIONS
  @Path("/{name}" + FilterRestService.PATH)
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForFilterRestService(@PathParam("name") String engineName, @Context UriInfo context) {
    return super.getFilterRestService(engineName).availableOperations(context);
  }

  @OPTIONS
  @Path("/{name}" + FilterRestService.PATH + "/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForFilterResource(@PathParam("name") String engineName,
                                                                 @PathParam("id") String filterId,
                                                                 @Context UriInfo context) {
    return super.getFilterRestService(engineName).getFilter(filterId).availableOperations(context);
  }

  @Override
  @Path("/{name}" + MetricsRestService.PATH)
  public MetricsRestService getMetricsRestService(@PathParam("name") String engineName) {
    return super.getMetricsRestService(engineName);
  }

  @Override
  @Path("/{name}" + DecisionDefinitionRestService.PATH)
  public DecisionDefinitionRestService getDecisionDefinitionRestService(@PathParam("name") String engineName) {
    return super.getDecisionDefinitionRestService(engineName);
  }

  @Override
  @Path("/{name}" + DecisionRequirementsDefinitionRestService.PATH)
  public DecisionRequirementsDefinitionRestService getDecisionRequirementsDefinitionRestService(@PathParam("name") String engineName) {
    return super.getDecisionRequirementsDefinitionRestService(engineName);
  }

  @Override
  @Path("/{name}" + ExternalTaskRestService.PATH)
  public ExternalTaskRestService getExternalTaskRestService(@PathParam("name") String engineName) {
    return super.getExternalTaskRestService(engineName);
  }

  @Override
  @Path("/{name}" + MigrationRestService.PATH)
  public MigrationRestService getMigrationRestService(@PathParam("name") String engineName) {
    return super.getMigrationRestService(engineName);
  }

  @Override
  @Path("/{name}" + BatchRestService.PATH)
  public BatchRestService getBatchRestService(@PathParam("name") String engineName) {
    return super.getBatchRestService(engineName);
  }

  @Override
  @Path("/{name}" + TenantRestService.PATH)
  public TenantRestService getTenantRestService(@PathParam("name") String engineName) {
    return super.getTenantRestService(engineName);
  }

  @OPTIONS
  @Path("/{name}" + TenantRestService.PATH)
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForTenantRestService(@PathParam("name") String engineName, @Context UriInfo context) {
    return super.getTenantRestService(engineName).availableOperations(context);
  }

  @OPTIONS
  @Path("/{name}" + TenantRestService.PATH + "/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForTenantResource(@PathParam("name") String engineName,
                                                                 @PathParam("id") String id,
                                                                 @Context UriInfo context) {
    return super.getTenantRestService(engineName).getTenant(id).availableOperations(context);
  }

  @OPTIONS
  @Path("/{name}" + TenantRestService.PATH + "/{id}" + TenantUserMembersResource.PATH)
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForTenantUserMembersResource(@PathParam("name") String engineName,
                                                                 @PathParam("id") String id,
                                                                 @Context UriInfo context) {
    return super.getTenantRestService(engineName).getTenant(id).getTenantUserMembersResource().availableOperations(context);
  }

  @OPTIONS
  @Path("/{name}" + TenantRestService.PATH + "/{id}" + TenantGroupMembersResource.PATH)
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForTenantGroupMembersResource(@PathParam("name") String engineName,
                                                                 @PathParam("id") String id,
                                                                 @Context UriInfo context) {
    return super.getTenantRestService(engineName).getTenant(id).getTenantGroupMembersResource().availableOperations(context);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<ProcessEngineDto> getProcessEngineNames() {
    ProcessEngineProvider provider = getProcessEngineProvider();
    Set<String> engineNames = provider.getProcessEngineNames();

    List<ProcessEngineDto> results = new ArrayList<ProcessEngineDto>();
    for (String engineName : engineNames) {
      ProcessEngineDto dto = new ProcessEngineDto();
      dto.setName(engineName);
      results.add(dto);
    }

    return results;
  }

  @Override
  protected URI getRelativeEngineUri(String engineName) {
    return UriBuilder.fromResource(NamedProcessEngineRestServiceImpl.class).path("{name}").build(engineName);
  }

  protected ProcessEngineProvider getProcessEngineProvider() {
    ServiceLoader<ProcessEngineProvider> serviceLoader = ServiceLoader.load(ProcessEngineProvider.class);
    Iterator<ProcessEngineProvider> iterator = serviceLoader.iterator();

    if(iterator.hasNext()) {
      ProcessEngineProvider provider = iterator.next();
      return provider;
    } else {
      throw new RestException(Status.INTERNAL_SERVER_ERROR, "No process engine provider found");
    }
  }

}
