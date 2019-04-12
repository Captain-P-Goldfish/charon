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
import org.wso2.charon3.core.exceptions.InternalErrorException;
import org.wso2.charon3.core.exceptions.NotFoundException;
import org.wso2.charon3.core.extensions.ResourceHandler;
import org.wso2.charon3.core.objects.AbstractSCIMObject;
import org.wso2.charon3.core.objects.ListedResource;
import org.wso2.charon3.core.protocol.ResponseCodeConstants;
import org.wso2.charon3.core.protocol.SCIMResponse;
import org.wso2.charon3.core.resourcetypes.ResourceType;
import org.wso2.charon3.core.schema.SCIMConstants;
import org.wso2.charon3.core.utils.LambdaExceptionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.wso2.charon3.core.schema.ServerSideValidator.validateResourceTypeSCIMObject;

/**
 * The "RESOURCE_TYPES" schema specifies the metadata about a resource type. This is the spec compatible version of
 * ResourceTypeResourceManager
 */
public class ResourceTypeResourceManager extends ResourceManager {


    public ResourceTypeResourceManager(ResourceHandler resourceHandler) {
        super(resourceHandler);
    }

    private static final Logger log = LoggerFactory.getLogger(ResourceTypeResourceManager.class);

    /**
     * Retrieves a resource type
     *
     * @return SCIM response to be returned.
     */
    //    @Override
    public SCIMResponse get(String id, ResourceHandler resourceHandler, String attributes, String excludeAttributes) {

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
    public SCIMResponse create(String scimObjectString,
                               UserManager userManager,
                               String attributes,
                               String excludeAttributes) {

        String error = "Request is undefined";
        BadRequestException badRequestException = new BadRequestException(error, ResponseCodeConstants.INVALID_PATH);
        return encodeSCIMException(badRequestException);
    }

    public SCIMResponse delete(ResourceHandler userManager, String id) {

        String error = "Request is undefined";
        BadRequestException badRequestException = new BadRequestException(error, ResponseCodeConstants.INVALID_PATH);
        return encodeSCIMException(badRequestException);
    }

    public SCIMResponse listWithGET(ResourceHandler userManager, String filter, int startIndex, int count,
                                    String sortBy, String sortOrder, String domainName, String attributes,
                                    String excludeAttributes) {

        String error = "Request is undefined";
        BadRequestException badRequestException = new BadRequestException(error, ResponseCodeConstants.INVALID_PATH);
        return encodeSCIMException(badRequestException);
    }

    public SCIMResponse listWithPOST(ResourceHandler userManager, String resourceString) {

        String error = "Request is undefined";
        BadRequestException badRequestException = new BadRequestException(error, ResponseCodeConstants.INVALID_PATH);
        return encodeSCIMException(badRequestException);
    }

    public SCIMResponse updateWithPUT(ResourceHandler userManager, String existingId, String scimObjectString,
                                      String attributes, String excludeAttributes) {

        String error = "Request is undefined";
        BadRequestException badRequestException = new BadRequestException(error, ResponseCodeConstants.INVALID_PATH);
        return encodeSCIMException(badRequestException);
    }

    public SCIMResponse updateWithPATCH(ResourceHandler userManager, String existingId, String scimObjectString,
                                        String attributes, String excludeAttributes) {

        String error = "Request is undefined";
        BadRequestException badRequestException = new BadRequestException(error, ResponseCodeConstants.INVALID_PATH);
        return encodeSCIMException(badRequestException);
    }

    /**
     * This combines the user and group resource type AbstractSCIMObjects and build a
     * one root AbstractSCIMObjects
     *
     * @param userObject
     * @param groupObject
     * @return
     * @throws CharonException
     */
    private AbstractSCIMObject buildCombinedResourceType(AbstractSCIMObject userObject, AbstractSCIMObject groupObject)
        throws CharonException {

        AbstractSCIMObject rootObject = new AbstractSCIMObject();
        MultiValuedAttribute multiValuedAttribute =
            new MultiValuedAttribute(SCIMConstants.ListedResourceSchemaConstants.RESOURCES);

        userObject.getSchemaList().clear();
        userObject.setSchema(SCIMConstants.RESOURCE_TYPE_SCHEMA_URI);
        multiValuedAttribute.setAttributePrimitiveValue(userObject);

        groupObject.getSchemaList().clear();
        groupObject.setSchema(SCIMConstants.RESOURCE_TYPE_SCHEMA_URI);
        multiValuedAttribute.setAttributePrimitiveValue(groupObject);

        rootObject.setAttribute(multiValuedAttribute);
        rootObject.setSchema(SCIMConstants.LISTED_RESOURCE_CORE_SCHEMA_URI);
        // Using a hard coded value of 2 since currently we only support two items in the list.
        SimpleAttribute totalResults = new SimpleAttribute(SCIMConstants.CommonSchemaConstants.TOTAL_RESULTS, 2);
        rootObject.setAttribute(totalResults);
        return rootObject;
    }
}
