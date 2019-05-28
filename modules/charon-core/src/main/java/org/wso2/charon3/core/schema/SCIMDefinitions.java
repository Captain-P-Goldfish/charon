/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.charon3.core.schema;

/**
 * this defines the pre-defined values specified in https://tools.ietf.org/html/rfc7643..
 */
public class SCIMDefinitions {

    /**
     * data types that an attribute can take, according to the SCIM spec..
     */
    public static enum DataType {
        STRING, BOOLEAN, DECIMAL, INTEGER, DATE_TIME, BINARY, REFERENCE, COMPLEX
    }

    /**
     * values that an attributes' mutability attribute can take..
     */
    public static enum Mutability {
        READ_WRITE, READ_ONLY, IMMUTABLE, WRITE_ONLY
    }

    /**
     * values that an attributes' returned attribute can take..
     */
    public static enum Returned {
        ALWAYS, NEVER, DEFAULT, REQUEST
    }

    /**
     * values that an attributes' uniqueness attribute can take..
     */
    public static enum Uniqueness {
        NONE, SERVER, GLOBAL
    }

    /**
     * SCIM resource types that a referenceType attribute that may be referenced..
     */
    public static enum ReferenceType {
        USER, GROUP, EXTERNAL, URI
    }

    /**
     * a SCIM filter expression used for building filters with
     * {@link org.wso2.charon3.core.utils.codeutils.FilterBuilder}.
     */
    public static enum FilterOperation {
        EQ, NE, CO, SW, EW, PR, GT, GE, LT, LE
    }

    /**
     * a SCIM filter expression used for building filters with
     * {@link org.wso2.charon3.core.utils.codeutils.FilterBuilder}.
     */
    public static enum FilterConcatenation {
        AND, OR, NOT
    }

}
