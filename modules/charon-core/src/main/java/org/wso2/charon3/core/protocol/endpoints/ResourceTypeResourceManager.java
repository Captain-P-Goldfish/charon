/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.charon3.core.protocol.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.charon3.core.config.ResourceTypeRegistration;
import org.wso2.charon3.core.exceptions.AbstractCharonException;
import org.wso2.charon3.core.exceptions.BadRequestException;
import org.wso2.charon3.core.exceptions.CharonException;
import org.wso2.charon3.core.extensions.ResourceHandler;
import org.wso2.charon3.core.objects.AbstractSCIMObject;
import org.wso2.charon3.core.objects.ListedResource;
import org.wso2.charon3.core.protocol.ResponseCodeConstants;
import org.wso2.charon3.core.protocol.SCIMResponse;
import org.wso2.charon3.core.resourcetypes.ResourceType;
import org.wso2.charon3.core.schema.SCIMConstants;
import org.wso2.charon3.core.schema.SCIMResourceTypeSchema;
import org.wso2.charon3.core.utils.LambdaExceptionUtils;
import org.wso2.charon3.core.utils.codeutils.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.wso2.charon3.core.schema.ServerSideValidator.validateResourceTypeSCIMObject;

/**
 * The "RESOURCE_TYPES" schema specifies the metadata about a resource type. This is the spec compatible version of
 * ResourceTypeResourceManager
 */
public class ResourceTypeResourceManager extends ResourceManager<ResourceType> {


    private static final Logger log = LoggerFactory.getLogger(ResourceTypeResourceManager.class);

    public ResourceTypeResourceManager() {
        super(new ResourceTypeHandler());
    }

    /**
     * Retrieves a resource type
     *
     * @return SCIM response to be returned.
     */
    //    @Override
    @Override
    public SCIMResponse get(String id, String attributes, String excludeAttributes) {
        return getResourceType();
    }

    /**
     * return RESOURCE_TYPE schema
     */
    private SCIMResponse getResourceType() {

        try {
            List<ResourceType> copiedResourceTypes = ResourceTypeRegistration.getResourceTypeListCopy();
            copiedResourceTypes.forEach(resourceType -> {
                LambdaExceptionUtils.rethrowConsumer(rt -> validateResourceTypeSCIMObject((AbstractSCIMObject) rt))
                                    .accept(resourceType);
            });
            //encode the newly created SCIM Resource Type object.
            ListedResource listedResource = new ListedResource();
            listedResource.setTotalResults(ResourceTypeRegistration.getResouceTypeCount());
            copiedResourceTypes.forEach(listedResource::addResource);
            String encodedObject = getEncoder().encodeSCIMObject(listedResource);
            Map<String, String> responseHeaders = new HashMap<>();
            responseHeaders.put(SCIMConstants.LOCATION_HEADER,
                getResourceEndpointURL(SCIMConstants.RESOURCE_TYPE_ENDPOINT));
            responseHeaders.put(SCIMConstants.CONTENT_TYPE_HEADER, SCIMConstants.APPLICATION_JSON);

            //put the uri of the resource type object in the response header parameter.
            return new SCIMResponse(ResponseCodeConstants.CODE_OK, encodedObject, responseHeaders);
        } catch (AbstractCharonException e) {
            return encodeSCIMException(e);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            CharonException charonException = new CharonException("an unexpected error occured: " + e.getMessage());
            return encodeSCIMException(charonException);
        }
    }

    @Override
    public SCIMResponse create(String scimObjectString, String attributes, String excludeAttributes) {

        String error = "Request is undefined";
        BadRequestException badRequestException = new BadRequestException(error, ResponseCodeConstants.INVALID_PATH);
        return encodeSCIMException(badRequestException);
    }

    @Override
    public SCIMResponse delete(String id) {

        String error = "Request is undefined";
        BadRequestException badRequestException = new BadRequestException(error, ResponseCodeConstants.INVALID_PATH);
        return encodeSCIMException(badRequestException);
    }

    @Override
    public SCIMResponse listWithGET(String filter,
                                    Integer startIndex,
                                    Integer count,
                                    String sortBy,
                                    String sortOrder,
                                    String domainName,
                                    String attributes,
                                    String excludeAttributes) {

        String error = "Request is undefined";
        BadRequestException badRequestException = new BadRequestException(error, ResponseCodeConstants.INVALID_PATH);
        return encodeSCIMException(badRequestException);
    }

    @Override
    public SCIMResponse listWithPOST(String resourceString) {

        String error = "Request is undefined";
        BadRequestException badRequestException = new BadRequestException(error, ResponseCodeConstants.INVALID_PATH);
        return encodeSCIMException(badRequestException);
    }

    @Override
    public SCIMResponse updateWithPUT(String existingId,
                                      String scimObjectString,
                                      String attributes,
                                      String excludeAttributes) {

        String error = "Request is undefined";
        BadRequestException badRequestException = new BadRequestException(error, ResponseCodeConstants.INVALID_PATH);
        return encodeSCIMException(badRequestException);
    }

    @Override
    public SCIMResponse updateWithPATCH(String existingId,
                                        String scimObjectString,
                                        String attributes,
                                        String excludeAttributes) {

        String error = "Request is undefined";
        BadRequestException badRequestException = new BadRequestException(error, ResponseCodeConstants.INVALID_PATH);
        return encodeSCIMException(badRequestException);
    }

    /**
     * empty useless implementation that is used to bypass the Objects.requireNonNull method in the constructor of
     * {@link ResourceManager}
     */
    private static class ResourceTypeHandler implements ResourceHandler<ResourceType> {

        @Override
        public ResourceType create(ResourceType resource, Map<String, Boolean> requiredAttributes)
            throws AbstractCharonException {
            return null;
        }

        @Override
        public ResourceType get(String id, Map<String, Boolean> requiredAttributes) throws AbstractCharonException {
            return null;
        }

        @Override
        public void delete(String id) throws AbstractCharonException {

        }

        @Override
        public List<Object> listResources(Node node,
                                          Integer startIndex,
                                          Integer count,
                                          String sortBy,
                                          String sortOrder,
                                          String domainName,
                                          Map<String, Boolean> requiredAttributes) throws AbstractCharonException {
            return null;
        }

        @Override
        public ResourceType update(ResourceType resourceUpdate, Map<String, Boolean> requiredAttributes)
            throws AbstractCharonException {
            return null;
        }

        @Override
        public String getResourceEndpoint() {
            return null;
        }

        @Override
        public SCIMResourceTypeSchema getResourceSchema() {
            return null;
        }
    }
}
