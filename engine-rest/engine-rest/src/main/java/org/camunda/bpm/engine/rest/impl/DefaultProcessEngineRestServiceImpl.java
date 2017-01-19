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

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.rest.sub.identity.GroupMembersResource;
import org.camunda.bpm.engine.rest.sub.identity.TenantGroupMembersResource;
import org.camunda.bpm.engine.rest.sub.identity.TenantUserMembersResource;

@Path(DefaultProcessEngineRestServiceImpl.PATH)
public class DefaultProcessEngineRestServiceImpl extends AbstractProcessEngineRestServiceImpl {

  public static final String PATH = "";

  @Path(ProcessDefinitionRestService.PATH)
  public ProcessDefinitionRestService getProcessDefinitionService() {
    return super.getProcessDefinitionService(null);
  }

  @Path(ProcessInstanceRestService.PATH)
  public ProcessInstanceRestService getProcessInstanceService() {
    return super.getProcessInstanceService(null);
  }

  @Path(ExecutionRestService.PATH)
  public ExecutionRestService getExecutionService() {
    return super.getExecutionService(null);
  }

  @Path(TaskRestService.PATH)
  public TaskRestService getTaskRestService() {
    return super.getTaskRestService(null);
  }

  @Path(IdentityRestService.PATH)
  public IdentityRestService getIdentityRestService() {
    return super.getIdentityRestService(null);
  }

  @Path(MessageRestService.PATH)
  public MessageRestService getMessageRestService() {
    return super.getMessageRestService(null);
  }

  @Path(VariableInstanceRestService.PATH)
  public VariableInstanceRestService getVariableInstanceService() {
    return super.getVariableInstanceService(null);
  }

  @Path(JobDefinitionRestService.PATH)
  public JobDefinitionRestService getJobDefinitionRestService() {
    return super.getJobDefinitionRestService(null);
  }

  @Path(JobRestService.PATH)
  public JobRestService getJobRestService() {
    return super.getJobRestService(null);
  }

//  @Path(GroupRestService.PATH)
//  public GroupRestService getGroupRestService() {
//    return super.getGroupRestService(null);
//  }
//
//  @OPTIONS
//  @Path(GroupRestService.PATH)
//  @Produces(MediaType.APPLICATION_JSON)
//  public ResourceOptionsDto availableOperationsForGroupRestService(@Context UriInfo context) {
//    return super.getGroupRestService(null).availableOperations(context);
//  }
//
//  @OPTIONS
//  @Path(GroupRestService.PATH + "/{id}")
//  @Produces(MediaType.APPLICATION_JSON)
//  public ResourceOptionsDto availableOperationsForGroupResource(@PathParam("id") String id,
//                                                                @Context UriInfo context) {
//    return super.getGroupRestService(null).getGroup(id).availableOperations(context);
//  }
//
//  @OPTIONS
//  @Path(GroupRestService.PATH + "/{id}" + GroupMembersResource.PATH)
//  @Produces(MediaType.APPLICATION_JSON)
//  public ResourceOptionsDto availableOperationsForGroupMemberResource(@PathParam("id") String id,
//                                                                      @Context UriInfo context) {
//    return super.getGroupRestService(null).getGroup(id).getGroupMembersResource().availableOperations(context);
//  }

  @Path(UserRestService.PATH)
  public UserRestService getUserRestService() {
    return super.getUserRestService(null);
  }

  @OPTIONS
  @Path(UserRestService.PATH)
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForUserRestService(@Context UriInfo context) {
    return super.getUserRestService(null).availableOperations(context);
  }

  @OPTIONS
  @Path(UserRestService.PATH + "/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForUserResource(@PathParam("id") String id, @Context UriInfo context) {
    return super.getUserRestService(null).getUser(id).availableOperations(context);
  }

  @Path(AuthorizationRestService.PATH)
  public AuthorizationRestService getAuthorizationRestService() {
    return super.getAuthorizationRestService(null);
  }

  @OPTIONS
  @Path(AuthorizationRestService.PATH)
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForAuthorizationRestService(@Context UriInfo context) {
    return super.getAuthorizationRestService(null).availableOperations(context);
  }

  @OPTIONS
  @Path(AuthorizationRestService.PATH + "/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForAuthorizationResource(@PathParam("id") String id,
                                                                        @Context UriInfo context) {
    return super.getAuthorizationRestService(null).getAuthorization(id).availableOperations(context);
  }

  @Path(IncidentRestService.PATH)
  public IncidentRestService getIncidentService() {
    return super.getIncidentService(null);
  }

  @Path(HistoryRestService.PATH)
  public HistoryRestService getHistoryRestService() {
    return super.getHistoryRestService(null);
  }

  @Path(DeploymentRestService.PATH)
  public DeploymentRestService getDeploymentRestService() {
    return super.getDeploymentRestService(null);
  }

  @Path(CaseDefinitionRestService.PATH)
  public CaseDefinitionRestService getCaseDefinitionRestService() {
    return super.getCaseDefinitionRestService(null);
  }

  @Path(CaseInstanceRestService.PATH)
  public CaseInstanceRestService getCaseInstanceRestService() {
    return super.getCaseInstanceRestService(null);
  }

  @Path(CaseExecutionRestService.PATH)
  public CaseExecutionRestService getCaseExecutionRestService() {
    return super.getCaseExecutionRestService(null);
  }

  @Path(FilterRestService.PATH)
  public FilterRestService getFilterRestService() {
    return super.getFilterRestService(null);
  }

  @OPTIONS
  @Path(FilterRestService.PATH)
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForFilterRestService(@Context UriInfo context) {
    return super.getFilterRestService(null).availableOperations(context);
  }

  @OPTIONS
  @Path(FilterRestService.PATH + "/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForFilterResource(@PathParam("id") String filterId,
                                                                 @Context UriInfo context) {
    return super.getFilterRestService(null).getFilter(filterId).availableOperations(context);
  }

  @Path(MetricsRestService.PATH)
  public MetricsRestService getMetricsRestService() {
    return super.getMetricsRestService(null);
  }

  @Path(DecisionDefinitionRestService.PATH)
  public DecisionDefinitionRestService getDecisionDefinitionRestService() {
    return super.getDecisionDefinitionRestService(null);
  }

  @Path(DecisionRequirementsDefinitionRestService.PATH)
  public DecisionRequirementsDefinitionRestService getDecisionRequirementsDefinitionRestService() {
    return super.getDecisionRequirementsDefinitionRestService(null);
  }

  @Path(ExternalTaskRestService.PATH)
  public ExternalTaskRestService getExternalTaskRestService() {
    return super.getExternalTaskRestService(null);
  }

  @Path(MigrationRestService.PATH)
  public MigrationRestService getMigrationRestService() {
    return super.getMigrationRestService(null);
  }

  @Path(BatchRestService.PATH)
  public BatchRestService getBatchRestService() {
    return super.getBatchRestService(null);
  }

  @Path(TenantRestService.PATH)
  public TenantRestService getTenantRestService() {
    return super.getTenantRestService(null);
  }

  @OPTIONS
  @Path(TenantRestService.PATH)
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForTenantRestService(@Context UriInfo context) {
    return super.getTenantRestService(null).availableOperations(context);
  }

  @OPTIONS
  @Path(TenantRestService.PATH + "/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForTenantResource(@PathParam("id") String id,
                                                                 @Context UriInfo context) {
    return super.getTenantRestService(null).getTenant(id).availableOperations(context);
  }

  @OPTIONS
  @Path(TenantRestService.PATH + "/{id}" + TenantUserMembersResource.PATH)
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForTenantUserMembersResource(@PathParam("id") String id,
                                                                            @Context UriInfo context) {
    return super.getTenantRestService(null).getTenant(id).getTenantUserMembersResource().availableOperations(context);
  }

  @OPTIONS
  @Path(TenantRestService.PATH + "/{id}" + TenantGroupMembersResource.PATH)
  @Produces(MediaType.APPLICATION_JSON)
  public ResourceOptionsDto availableOperationsForTenantGroupMembersResource(@PathParam("id") String id,
                                                                             @Context UriInfo context) {
    return super.getTenantRestService(null).getTenant(id).getTenantGroupMembersResource().availableOperations(context);
  }

  @Override
  protected URI getRelativeEngineUri(String engineName) {
    // the default engine
    return URI.create("/");
  }
}
