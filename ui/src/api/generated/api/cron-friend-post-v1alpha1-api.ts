/* tslint:disable */
/* eslint-disable */
/**
 * Halo
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 2.20.11
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


import type { Configuration } from '../configuration';
import type { AxiosPromise, AxiosInstance, RawAxiosRequestConfig } from 'axios';
import globalAxios from 'axios';
// Some imports not used depending on template conditions
// @ts-ignore
import { DUMMY_BASE_URL, assertParamExists, setApiKeyToObject, setBasicAuthToObject, setBearerAuthToObject, setOAuthToObject, setSearchParams, serializeDataIfNeeded, toPathString, createRequestFunction } from '../common';
// @ts-ignore
import { BASE_PATH, COLLECTION_FORMATS, type RequestArgs, BaseAPI, RequiredError, operationServerMap } from '../base';
// @ts-ignore
import type { CronFriendPost } from '../models';
// @ts-ignore
import type { CronFriendPostList } from '../models';
// @ts-ignore
import type { JsonPatchInner } from '../models';
/**
 * CronFriendPostV1alpha1Api - axios parameter creator
 * @export
 */
export const CronFriendPostV1alpha1ApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * Create CronFriendPost
         * @param {CronFriendPost} [cronFriendPost] Fresh cronfriendpost
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createCronFriendPost: async (cronFriendPost?: CronFriendPost, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/apis/friend.moony.la/v1alpha1/cronfriendposts`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'POST', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication basicAuth required
            // http basic authentication required
            setBasicAuthToObject(localVarRequestOptions, configuration)

            // authentication bearerAuth required
            // http bearer authentication required
            await setBearerAuthToObject(localVarHeaderParameter, configuration)


    
            localVarHeaderParameter['Content-Type'] = 'application/json';

            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(cronFriendPost, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * Delete CronFriendPost
         * @param {string} name Name of cronfriendpost
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        deleteCronFriendPost: async (name: string, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'name' is not null or undefined
            assertParamExists('deleteCronFriendPost', 'name', name)
            const localVarPath = `/apis/friend.moony.la/v1alpha1/cronfriendposts/{name}`
                .replace(`{${"name"}}`, encodeURIComponent(String(name)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'DELETE', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication basicAuth required
            // http basic authentication required
            setBasicAuthToObject(localVarRequestOptions, configuration)

            // authentication bearerAuth required
            // http bearer authentication required
            await setBearerAuthToObject(localVarHeaderParameter, configuration)


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * Get CronFriendPost
         * @param {string} name Name of cronfriendpost
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getCronFriendPost: async (name: string, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'name' is not null or undefined
            assertParamExists('getCronFriendPost', 'name', name)
            const localVarPath = `/apis/friend.moony.la/v1alpha1/cronfriendposts/{name}`
                .replace(`{${"name"}}`, encodeURIComponent(String(name)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication basicAuth required
            // http basic authentication required
            setBasicAuthToObject(localVarRequestOptions, configuration)

            // authentication bearerAuth required
            // http bearer authentication required
            await setBearerAuthToObject(localVarHeaderParameter, configuration)


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * List CronFriendPost
         * @param {number} [page] Page number. Default is 0.
         * @param {number} [size] Size number. Default is 0.
         * @param {Array<string>} [labelSelector] Label selector. e.g.: hidden!&#x3D;true
         * @param {Array<string>} [fieldSelector] Field selector. e.g.: metadata.name&#x3D;&#x3D;halo
         * @param {Array<string>} [sort] Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        listCronFriendPost: async (page?: number, size?: number, labelSelector?: Array<string>, fieldSelector?: Array<string>, sort?: Array<string>, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/apis/friend.moony.la/v1alpha1/cronfriendposts`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication basicAuth required
            // http basic authentication required
            setBasicAuthToObject(localVarRequestOptions, configuration)

            // authentication bearerAuth required
            // http bearer authentication required
            await setBearerAuthToObject(localVarHeaderParameter, configuration)

            if (page !== undefined) {
                localVarQueryParameter['page'] = page;
            }

            if (size !== undefined) {
                localVarQueryParameter['size'] = size;
            }

            if (labelSelector) {
                localVarQueryParameter['labelSelector'] = labelSelector;
            }

            if (fieldSelector) {
                localVarQueryParameter['fieldSelector'] = fieldSelector;
            }

            if (sort) {
                localVarQueryParameter['sort'] = sort;
            }


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * Patch CronFriendPost
         * @param {string} name Name of cronfriendpost
         * @param {Array<JsonPatchInner>} [jsonPatchInner] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        patchCronFriendPost: async (name: string, jsonPatchInner?: Array<JsonPatchInner>, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'name' is not null or undefined
            assertParamExists('patchCronFriendPost', 'name', name)
            const localVarPath = `/apis/friend.moony.la/v1alpha1/cronfriendposts/{name}`
                .replace(`{${"name"}}`, encodeURIComponent(String(name)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'PATCH', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication basicAuth required
            // http basic authentication required
            setBasicAuthToObject(localVarRequestOptions, configuration)

            // authentication bearerAuth required
            // http bearer authentication required
            await setBearerAuthToObject(localVarHeaderParameter, configuration)


    
            localVarHeaderParameter['Content-Type'] = 'application/json-patch+json';

            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(jsonPatchInner, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * Update CronFriendPost
         * @param {string} name Name of cronfriendpost
         * @param {CronFriendPost} [cronFriendPost] Updated cronfriendpost
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        updateCronFriendPost: async (name: string, cronFriendPost?: CronFriendPost, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'name' is not null or undefined
            assertParamExists('updateCronFriendPost', 'name', name)
            const localVarPath = `/apis/friend.moony.la/v1alpha1/cronfriendposts/{name}`
                .replace(`{${"name"}}`, encodeURIComponent(String(name)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'PUT', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication basicAuth required
            // http basic authentication required
            setBasicAuthToObject(localVarRequestOptions, configuration)

            // authentication bearerAuth required
            // http bearer authentication required
            await setBearerAuthToObject(localVarHeaderParameter, configuration)


    
            localVarHeaderParameter['Content-Type'] = 'application/json';

            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(cronFriendPost, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
    }
};

/**
 * CronFriendPostV1alpha1Api - functional programming interface
 * @export
 */
export const CronFriendPostV1alpha1ApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = CronFriendPostV1alpha1ApiAxiosParamCreator(configuration)
    return {
        /**
         * Create CronFriendPost
         * @param {CronFriendPost} [cronFriendPost] Fresh cronfriendpost
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async createCronFriendPost(cronFriendPost?: CronFriendPost, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<CronFriendPost>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.createCronFriendPost(cronFriendPost, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['CronFriendPostV1alpha1Api.createCronFriendPost']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * Delete CronFriendPost
         * @param {string} name Name of cronfriendpost
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async deleteCronFriendPost(name: string, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.deleteCronFriendPost(name, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['CronFriendPostV1alpha1Api.deleteCronFriendPost']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * Get CronFriendPost
         * @param {string} name Name of cronfriendpost
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getCronFriendPost(name: string, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<CronFriendPost>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getCronFriendPost(name, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['CronFriendPostV1alpha1Api.getCronFriendPost']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * List CronFriendPost
         * @param {number} [page] Page number. Default is 0.
         * @param {number} [size] Size number. Default is 0.
         * @param {Array<string>} [labelSelector] Label selector. e.g.: hidden!&#x3D;true
         * @param {Array<string>} [fieldSelector] Field selector. e.g.: metadata.name&#x3D;&#x3D;halo
         * @param {Array<string>} [sort] Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async listCronFriendPost(page?: number, size?: number, labelSelector?: Array<string>, fieldSelector?: Array<string>, sort?: Array<string>, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<CronFriendPostList>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.listCronFriendPost(page, size, labelSelector, fieldSelector, sort, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['CronFriendPostV1alpha1Api.listCronFriendPost']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * Patch CronFriendPost
         * @param {string} name Name of cronfriendpost
         * @param {Array<JsonPatchInner>} [jsonPatchInner] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async patchCronFriendPost(name: string, jsonPatchInner?: Array<JsonPatchInner>, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<CronFriendPost>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.patchCronFriendPost(name, jsonPatchInner, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['CronFriendPostV1alpha1Api.patchCronFriendPost']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * Update CronFriendPost
         * @param {string} name Name of cronfriendpost
         * @param {CronFriendPost} [cronFriendPost] Updated cronfriendpost
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async updateCronFriendPost(name: string, cronFriendPost?: CronFriendPost, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<CronFriendPost>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.updateCronFriendPost(name, cronFriendPost, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['CronFriendPostV1alpha1Api.updateCronFriendPost']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
    }
};

/**
 * CronFriendPostV1alpha1Api - factory interface
 * @export
 */
export const CronFriendPostV1alpha1ApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = CronFriendPostV1alpha1ApiFp(configuration)
    return {
        /**
         * Create CronFriendPost
         * @param {CronFriendPostV1alpha1ApiCreateCronFriendPostRequest} requestParameters Request parameters.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createCronFriendPost(requestParameters: CronFriendPostV1alpha1ApiCreateCronFriendPostRequest = {}, options?: RawAxiosRequestConfig): AxiosPromise<CronFriendPost> {
            return localVarFp.createCronFriendPost(requestParameters.cronFriendPost, options).then((request) => request(axios, basePath));
        },
        /**
         * Delete CronFriendPost
         * @param {CronFriendPostV1alpha1ApiDeleteCronFriendPostRequest} requestParameters Request parameters.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        deleteCronFriendPost(requestParameters: CronFriendPostV1alpha1ApiDeleteCronFriendPostRequest, options?: RawAxiosRequestConfig): AxiosPromise<void> {
            return localVarFp.deleteCronFriendPost(requestParameters.name, options).then((request) => request(axios, basePath));
        },
        /**
         * Get CronFriendPost
         * @param {CronFriendPostV1alpha1ApiGetCronFriendPostRequest} requestParameters Request parameters.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getCronFriendPost(requestParameters: CronFriendPostV1alpha1ApiGetCronFriendPostRequest, options?: RawAxiosRequestConfig): AxiosPromise<CronFriendPost> {
            return localVarFp.getCronFriendPost(requestParameters.name, options).then((request) => request(axios, basePath));
        },
        /**
         * List CronFriendPost
         * @param {CronFriendPostV1alpha1ApiListCronFriendPostRequest} requestParameters Request parameters.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        listCronFriendPost(requestParameters: CronFriendPostV1alpha1ApiListCronFriendPostRequest = {}, options?: RawAxiosRequestConfig): AxiosPromise<CronFriendPostList> {
            return localVarFp.listCronFriendPost(requestParameters.page, requestParameters.size, requestParameters.labelSelector, requestParameters.fieldSelector, requestParameters.sort, options).then((request) => request(axios, basePath));
        },
        /**
         * Patch CronFriendPost
         * @param {CronFriendPostV1alpha1ApiPatchCronFriendPostRequest} requestParameters Request parameters.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        patchCronFriendPost(requestParameters: CronFriendPostV1alpha1ApiPatchCronFriendPostRequest, options?: RawAxiosRequestConfig): AxiosPromise<CronFriendPost> {
            return localVarFp.patchCronFriendPost(requestParameters.name, requestParameters.jsonPatchInner, options).then((request) => request(axios, basePath));
        },
        /**
         * Update CronFriendPost
         * @param {CronFriendPostV1alpha1ApiUpdateCronFriendPostRequest} requestParameters Request parameters.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        updateCronFriendPost(requestParameters: CronFriendPostV1alpha1ApiUpdateCronFriendPostRequest, options?: RawAxiosRequestConfig): AxiosPromise<CronFriendPost> {
            return localVarFp.updateCronFriendPost(requestParameters.name, requestParameters.cronFriendPost, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * Request parameters for createCronFriendPost operation in CronFriendPostV1alpha1Api.
 * @export
 * @interface CronFriendPostV1alpha1ApiCreateCronFriendPostRequest
 */
export interface CronFriendPostV1alpha1ApiCreateCronFriendPostRequest {
    /**
     * Fresh cronfriendpost
     * @type {CronFriendPost}
     * @memberof CronFriendPostV1alpha1ApiCreateCronFriendPost
     */
    readonly cronFriendPost?: CronFriendPost
}

/**
 * Request parameters for deleteCronFriendPost operation in CronFriendPostV1alpha1Api.
 * @export
 * @interface CronFriendPostV1alpha1ApiDeleteCronFriendPostRequest
 */
export interface CronFriendPostV1alpha1ApiDeleteCronFriendPostRequest {
    /**
     * Name of cronfriendpost
     * @type {string}
     * @memberof CronFriendPostV1alpha1ApiDeleteCronFriendPost
     */
    readonly name: string
}

/**
 * Request parameters for getCronFriendPost operation in CronFriendPostV1alpha1Api.
 * @export
 * @interface CronFriendPostV1alpha1ApiGetCronFriendPostRequest
 */
export interface CronFriendPostV1alpha1ApiGetCronFriendPostRequest {
    /**
     * Name of cronfriendpost
     * @type {string}
     * @memberof CronFriendPostV1alpha1ApiGetCronFriendPost
     */
    readonly name: string
}

/**
 * Request parameters for listCronFriendPost operation in CronFriendPostV1alpha1Api.
 * @export
 * @interface CronFriendPostV1alpha1ApiListCronFriendPostRequest
 */
export interface CronFriendPostV1alpha1ApiListCronFriendPostRequest {
    /**
     * Page number. Default is 0.
     * @type {number}
     * @memberof CronFriendPostV1alpha1ApiListCronFriendPost
     */
    readonly page?: number

    /**
     * Size number. Default is 0.
     * @type {number}
     * @memberof CronFriendPostV1alpha1ApiListCronFriendPost
     */
    readonly size?: number

    /**
     * Label selector. e.g.: hidden!&#x3D;true
     * @type {Array<string>}
     * @memberof CronFriendPostV1alpha1ApiListCronFriendPost
     */
    readonly labelSelector?: Array<string>

    /**
     * Field selector. e.g.: metadata.name&#x3D;&#x3D;halo
     * @type {Array<string>}
     * @memberof CronFriendPostV1alpha1ApiListCronFriendPost
     */
    readonly fieldSelector?: Array<string>

    /**
     * Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     * @type {Array<string>}
     * @memberof CronFriendPostV1alpha1ApiListCronFriendPost
     */
    readonly sort?: Array<string>
}

/**
 * Request parameters for patchCronFriendPost operation in CronFriendPostV1alpha1Api.
 * @export
 * @interface CronFriendPostV1alpha1ApiPatchCronFriendPostRequest
 */
export interface CronFriendPostV1alpha1ApiPatchCronFriendPostRequest {
    /**
     * Name of cronfriendpost
     * @type {string}
     * @memberof CronFriendPostV1alpha1ApiPatchCronFriendPost
     */
    readonly name: string

    /**
     * 
     * @type {Array<JsonPatchInner>}
     * @memberof CronFriendPostV1alpha1ApiPatchCronFriendPost
     */
    readonly jsonPatchInner?: Array<JsonPatchInner>
}

/**
 * Request parameters for updateCronFriendPost operation in CronFriendPostV1alpha1Api.
 * @export
 * @interface CronFriendPostV1alpha1ApiUpdateCronFriendPostRequest
 */
export interface CronFriendPostV1alpha1ApiUpdateCronFriendPostRequest {
    /**
     * Name of cronfriendpost
     * @type {string}
     * @memberof CronFriendPostV1alpha1ApiUpdateCronFriendPost
     */
    readonly name: string

    /**
     * Updated cronfriendpost
     * @type {CronFriendPost}
     * @memberof CronFriendPostV1alpha1ApiUpdateCronFriendPost
     */
    readonly cronFriendPost?: CronFriendPost
}

/**
 * CronFriendPostV1alpha1Api - object-oriented interface
 * @export
 * @class CronFriendPostV1alpha1Api
 * @extends {BaseAPI}
 */
export class CronFriendPostV1alpha1Api extends BaseAPI {
    /**
     * Create CronFriendPost
     * @param {CronFriendPostV1alpha1ApiCreateCronFriendPostRequest} requestParameters Request parameters.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof CronFriendPostV1alpha1Api
     */
    public createCronFriendPost(requestParameters: CronFriendPostV1alpha1ApiCreateCronFriendPostRequest = {}, options?: RawAxiosRequestConfig) {
        return CronFriendPostV1alpha1ApiFp(this.configuration).createCronFriendPost(requestParameters.cronFriendPost, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * Delete CronFriendPost
     * @param {CronFriendPostV1alpha1ApiDeleteCronFriendPostRequest} requestParameters Request parameters.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof CronFriendPostV1alpha1Api
     */
    public deleteCronFriendPost(requestParameters: CronFriendPostV1alpha1ApiDeleteCronFriendPostRequest, options?: RawAxiosRequestConfig) {
        return CronFriendPostV1alpha1ApiFp(this.configuration).deleteCronFriendPost(requestParameters.name, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * Get CronFriendPost
     * @param {CronFriendPostV1alpha1ApiGetCronFriendPostRequest} requestParameters Request parameters.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof CronFriendPostV1alpha1Api
     */
    public getCronFriendPost(requestParameters: CronFriendPostV1alpha1ApiGetCronFriendPostRequest, options?: RawAxiosRequestConfig) {
        return CronFriendPostV1alpha1ApiFp(this.configuration).getCronFriendPost(requestParameters.name, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * List CronFriendPost
     * @param {CronFriendPostV1alpha1ApiListCronFriendPostRequest} requestParameters Request parameters.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof CronFriendPostV1alpha1Api
     */
    public listCronFriendPost(requestParameters: CronFriendPostV1alpha1ApiListCronFriendPostRequest = {}, options?: RawAxiosRequestConfig) {
        return CronFriendPostV1alpha1ApiFp(this.configuration).listCronFriendPost(requestParameters.page, requestParameters.size, requestParameters.labelSelector, requestParameters.fieldSelector, requestParameters.sort, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * Patch CronFriendPost
     * @param {CronFriendPostV1alpha1ApiPatchCronFriendPostRequest} requestParameters Request parameters.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof CronFriendPostV1alpha1Api
     */
    public patchCronFriendPost(requestParameters: CronFriendPostV1alpha1ApiPatchCronFriendPostRequest, options?: RawAxiosRequestConfig) {
        return CronFriendPostV1alpha1ApiFp(this.configuration).patchCronFriendPost(requestParameters.name, requestParameters.jsonPatchInner, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * Update CronFriendPost
     * @param {CronFriendPostV1alpha1ApiUpdateCronFriendPostRequest} requestParameters Request parameters.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof CronFriendPostV1alpha1Api
     */
    public updateCronFriendPost(requestParameters: CronFriendPostV1alpha1ApiUpdateCronFriendPostRequest, options?: RawAxiosRequestConfig) {
        return CronFriendPostV1alpha1ApiFp(this.configuration).updateCronFriendPost(requestParameters.name, requestParameters.cronFriendPost, options).then((request) => request(this.axios, this.basePath));
    }
}

