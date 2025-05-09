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
import type { FriendPostList } from '../models';
// @ts-ignore
import type { ListedRssSyncLogList } from '../models';
// @ts-ignore
import type { RssDetail } from '../models';
/**
 * ApiFriendMoonyLaV1alpha1FriendPostApi - axios parameter creator
 * @export
 */
export const ApiFriendMoonyLaV1alpha1FriendPostApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * Delete All RssFeedSyncLog.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        deleteAllRssFeedSyncLog: async (options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/apis/api.friend.moony.la/v1alpha1/friendposts/-/delete`;
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
         * List friendPost.
         * @param {number} [page] Page number. Default is 0.
         * @param {number} [size] Size number. Default is 0.
         * @param {Array<string>} [labelSelector] Label selector. e.g.: hidden!&#x3D;true
         * @param {Array<string>} [fieldSelector] Field selector. e.g.: metadata.name&#x3D;&#x3D;halo
         * @param {Array<string>} [sort] Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
         * @param {string} [linkName] CronFriendPost filtered by linkName.
         * @param {string} [keyword] CronFriendPost filtered by keyword.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        listFriendPosts: async (page?: number, size?: number, labelSelector?: Array<string>, fieldSelector?: Array<string>, sort?: Array<string>, linkName?: string, keyword?: string, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/apis/api.friend.moony.la/v1alpha1/friendposts`;
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

            if (linkName !== undefined) {
                localVarQueryParameter['linkName'] = linkName;
            }

            if (keyword !== undefined) {
                localVarQueryParameter['keyword'] = keyword;
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
         * List RssSyncLog.
         * @param {number} [page] Page number. Default is 0.
         * @param {number} [size] Size number. Default is 0.
         * @param {Array<string>} [labelSelector] Label selector. e.g.: hidden!&#x3D;true
         * @param {Array<string>} [fieldSelector] Field selector. e.g.: metadata.name&#x3D;&#x3D;halo
         * @param {Array<string>} [sort] Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
         * @param {string} [keyword] 
         * @param {string} [state] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        listRssSyncLogs: async (page?: number, size?: number, labelSelector?: Array<string>, fieldSelector?: Array<string>, sort?: Array<string>, keyword?: string, state?: string, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/apis/api.friend.moony.la/v1alpha1/rsssynclogs`;
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

            if (keyword !== undefined) {
                localVarQueryParameter['keyword'] = keyword;
            }

            if (state !== undefined) {
                localVarQueryParameter['state'] = state;
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
         * 
         * @param {string} rssUrl 
         * @param {string} [fetchLimitNumber] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        parsingRss: async (rssUrl: string, fetchLimitNumber?: string, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'rssUrl' is not null or undefined
            assertParamExists('parsingRss', 'rssUrl', rssUrl)
            const localVarPath = `/apis/api.friend.moony.la/v1alpha1/parsingrss`;
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

            if (rssUrl !== undefined) {
                localVarQueryParameter['rssUrl'] = rssUrl;
            }

            if (fetchLimitNumber !== undefined) {
                localVarQueryParameter['fetchLimitNumber'] = fetchLimitNumber;
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
         * 
         * @param {string} name 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        syncRssFeed: async (name: string, options: RawAxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'name' is not null or undefined
            assertParamExists('syncRssFeed', 'name', name)
            const localVarPath = `/apis/api.friend.moony.la/v1alpha1/syncrssfeed/{name}`
                .replace(`{${"name"}}`, encodeURIComponent(String(name)));
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


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
    }
};

/**
 * ApiFriendMoonyLaV1alpha1FriendPostApi - functional programming interface
 * @export
 */
export const ApiFriendMoonyLaV1alpha1FriendPostApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = ApiFriendMoonyLaV1alpha1FriendPostApiAxiosParamCreator(configuration)
    return {
        /**
         * Delete All RssFeedSyncLog.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async deleteAllRssFeedSyncLog(options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.deleteAllRssFeedSyncLog(options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['ApiFriendMoonyLaV1alpha1FriendPostApi.deleteAllRssFeedSyncLog']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * List friendPost.
         * @param {number} [page] Page number. Default is 0.
         * @param {number} [size] Size number. Default is 0.
         * @param {Array<string>} [labelSelector] Label selector. e.g.: hidden!&#x3D;true
         * @param {Array<string>} [fieldSelector] Field selector. e.g.: metadata.name&#x3D;&#x3D;halo
         * @param {Array<string>} [sort] Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
         * @param {string} [linkName] CronFriendPost filtered by linkName.
         * @param {string} [keyword] CronFriendPost filtered by keyword.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async listFriendPosts(page?: number, size?: number, labelSelector?: Array<string>, fieldSelector?: Array<string>, sort?: Array<string>, linkName?: string, keyword?: string, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<FriendPostList>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.listFriendPosts(page, size, labelSelector, fieldSelector, sort, linkName, keyword, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['ApiFriendMoonyLaV1alpha1FriendPostApi.listFriendPosts']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * List RssSyncLog.
         * @param {number} [page] Page number. Default is 0.
         * @param {number} [size] Size number. Default is 0.
         * @param {Array<string>} [labelSelector] Label selector. e.g.: hidden!&#x3D;true
         * @param {Array<string>} [fieldSelector] Field selector. e.g.: metadata.name&#x3D;&#x3D;halo
         * @param {Array<string>} [sort] Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
         * @param {string} [keyword] 
         * @param {string} [state] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async listRssSyncLogs(page?: number, size?: number, labelSelector?: Array<string>, fieldSelector?: Array<string>, sort?: Array<string>, keyword?: string, state?: string, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<ListedRssSyncLogList>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.listRssSyncLogs(page, size, labelSelector, fieldSelector, sort, keyword, state, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['ApiFriendMoonyLaV1alpha1FriendPostApi.listRssSyncLogs']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * 
         * @param {string} rssUrl 
         * @param {string} [fetchLimitNumber] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async parsingRss(rssUrl: string, fetchLimitNumber?: string, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<RssDetail>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.parsingRss(rssUrl, fetchLimitNumber, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['ApiFriendMoonyLaV1alpha1FriendPostApi.parsingRss']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
        /**
         * 
         * @param {string} name 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async syncRssFeed(name: string, options?: RawAxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.syncRssFeed(name, options);
            const localVarOperationServerIndex = configuration?.serverIndex ?? 0;
            const localVarOperationServerBasePath = operationServerMap['ApiFriendMoonyLaV1alpha1FriendPostApi.syncRssFeed']?.[localVarOperationServerIndex]?.url;
            return (axios, basePath) => createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration)(axios, localVarOperationServerBasePath || basePath);
        },
    }
};

/**
 * ApiFriendMoonyLaV1alpha1FriendPostApi - factory interface
 * @export
 */
export const ApiFriendMoonyLaV1alpha1FriendPostApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = ApiFriendMoonyLaV1alpha1FriendPostApiFp(configuration)
    return {
        /**
         * Delete All RssFeedSyncLog.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        deleteAllRssFeedSyncLog(options?: RawAxiosRequestConfig): AxiosPromise<void> {
            return localVarFp.deleteAllRssFeedSyncLog(options).then((request) => request(axios, basePath));
        },
        /**
         * List friendPost.
         * @param {ApiFriendMoonyLaV1alpha1FriendPostApiListFriendPostsRequest} requestParameters Request parameters.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        listFriendPosts(requestParameters: ApiFriendMoonyLaV1alpha1FriendPostApiListFriendPostsRequest = {}, options?: RawAxiosRequestConfig): AxiosPromise<FriendPostList> {
            return localVarFp.listFriendPosts(requestParameters.page, requestParameters.size, requestParameters.labelSelector, requestParameters.fieldSelector, requestParameters.sort, requestParameters.linkName, requestParameters.keyword, options).then((request) => request(axios, basePath));
        },
        /**
         * List RssSyncLog.
         * @param {ApiFriendMoonyLaV1alpha1FriendPostApiListRssSyncLogsRequest} requestParameters Request parameters.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        listRssSyncLogs(requestParameters: ApiFriendMoonyLaV1alpha1FriendPostApiListRssSyncLogsRequest = {}, options?: RawAxiosRequestConfig): AxiosPromise<ListedRssSyncLogList> {
            return localVarFp.listRssSyncLogs(requestParameters.page, requestParameters.size, requestParameters.labelSelector, requestParameters.fieldSelector, requestParameters.sort, requestParameters.keyword, requestParameters.state, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @param {ApiFriendMoonyLaV1alpha1FriendPostApiParsingRssRequest} requestParameters Request parameters.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        parsingRss(requestParameters: ApiFriendMoonyLaV1alpha1FriendPostApiParsingRssRequest, options?: RawAxiosRequestConfig): AxiosPromise<RssDetail> {
            return localVarFp.parsingRss(requestParameters.rssUrl, requestParameters.fetchLimitNumber, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @param {ApiFriendMoonyLaV1alpha1FriendPostApiSyncRssFeedRequest} requestParameters Request parameters.
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        syncRssFeed(requestParameters: ApiFriendMoonyLaV1alpha1FriendPostApiSyncRssFeedRequest, options?: RawAxiosRequestConfig): AxiosPromise<void> {
            return localVarFp.syncRssFeed(requestParameters.name, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * Request parameters for listFriendPosts operation in ApiFriendMoonyLaV1alpha1FriendPostApi.
 * @export
 * @interface ApiFriendMoonyLaV1alpha1FriendPostApiListFriendPostsRequest
 */
export interface ApiFriendMoonyLaV1alpha1FriendPostApiListFriendPostsRequest {
    /**
     * Page number. Default is 0.
     * @type {number}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiListFriendPosts
     */
    readonly page?: number

    /**
     * Size number. Default is 0.
     * @type {number}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiListFriendPosts
     */
    readonly size?: number

    /**
     * Label selector. e.g.: hidden!&#x3D;true
     * @type {Array<string>}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiListFriendPosts
     */
    readonly labelSelector?: Array<string>

    /**
     * Field selector. e.g.: metadata.name&#x3D;&#x3D;halo
     * @type {Array<string>}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiListFriendPosts
     */
    readonly fieldSelector?: Array<string>

    /**
     * Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     * @type {Array<string>}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiListFriendPosts
     */
    readonly sort?: Array<string>

    /**
     * CronFriendPost filtered by linkName.
     * @type {string}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiListFriendPosts
     */
    readonly linkName?: string

    /**
     * CronFriendPost filtered by keyword.
     * @type {string}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiListFriendPosts
     */
    readonly keyword?: string
}

/**
 * Request parameters for listRssSyncLogs operation in ApiFriendMoonyLaV1alpha1FriendPostApi.
 * @export
 * @interface ApiFriendMoonyLaV1alpha1FriendPostApiListRssSyncLogsRequest
 */
export interface ApiFriendMoonyLaV1alpha1FriendPostApiListRssSyncLogsRequest {
    /**
     * Page number. Default is 0.
     * @type {number}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiListRssSyncLogs
     */
    readonly page?: number

    /**
     * Size number. Default is 0.
     * @type {number}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiListRssSyncLogs
     */
    readonly size?: number

    /**
     * Label selector. e.g.: hidden!&#x3D;true
     * @type {Array<string>}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiListRssSyncLogs
     */
    readonly labelSelector?: Array<string>

    /**
     * Field selector. e.g.: metadata.name&#x3D;&#x3D;halo
     * @type {Array<string>}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiListRssSyncLogs
     */
    readonly fieldSelector?: Array<string>

    /**
     * Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     * @type {Array<string>}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiListRssSyncLogs
     */
    readonly sort?: Array<string>

    /**
     * 
     * @type {string}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiListRssSyncLogs
     */
    readonly keyword?: string

    /**
     * 
     * @type {string}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiListRssSyncLogs
     */
    readonly state?: string
}

/**
 * Request parameters for parsingRss operation in ApiFriendMoonyLaV1alpha1FriendPostApi.
 * @export
 * @interface ApiFriendMoonyLaV1alpha1FriendPostApiParsingRssRequest
 */
export interface ApiFriendMoonyLaV1alpha1FriendPostApiParsingRssRequest {
    /**
     * 
     * @type {string}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiParsingRss
     */
    readonly rssUrl: string

    /**
     * 
     * @type {string}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiParsingRss
     */
    readonly fetchLimitNumber?: string
}

/**
 * Request parameters for syncRssFeed operation in ApiFriendMoonyLaV1alpha1FriendPostApi.
 * @export
 * @interface ApiFriendMoonyLaV1alpha1FriendPostApiSyncRssFeedRequest
 */
export interface ApiFriendMoonyLaV1alpha1FriendPostApiSyncRssFeedRequest {
    /**
     * 
     * @type {string}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApiSyncRssFeed
     */
    readonly name: string
}

/**
 * ApiFriendMoonyLaV1alpha1FriendPostApi - object-oriented interface
 * @export
 * @class ApiFriendMoonyLaV1alpha1FriendPostApi
 * @extends {BaseAPI}
 */
export class ApiFriendMoonyLaV1alpha1FriendPostApi extends BaseAPI {
    /**
     * Delete All RssFeedSyncLog.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApi
     */
    public deleteAllRssFeedSyncLog(options?: RawAxiosRequestConfig) {
        return ApiFriendMoonyLaV1alpha1FriendPostApiFp(this.configuration).deleteAllRssFeedSyncLog(options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * List friendPost.
     * @param {ApiFriendMoonyLaV1alpha1FriendPostApiListFriendPostsRequest} requestParameters Request parameters.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApi
     */
    public listFriendPosts(requestParameters: ApiFriendMoonyLaV1alpha1FriendPostApiListFriendPostsRequest = {}, options?: RawAxiosRequestConfig) {
        return ApiFriendMoonyLaV1alpha1FriendPostApiFp(this.configuration).listFriendPosts(requestParameters.page, requestParameters.size, requestParameters.labelSelector, requestParameters.fieldSelector, requestParameters.sort, requestParameters.linkName, requestParameters.keyword, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * List RssSyncLog.
     * @param {ApiFriendMoonyLaV1alpha1FriendPostApiListRssSyncLogsRequest} requestParameters Request parameters.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApi
     */
    public listRssSyncLogs(requestParameters: ApiFriendMoonyLaV1alpha1FriendPostApiListRssSyncLogsRequest = {}, options?: RawAxiosRequestConfig) {
        return ApiFriendMoonyLaV1alpha1FriendPostApiFp(this.configuration).listRssSyncLogs(requestParameters.page, requestParameters.size, requestParameters.labelSelector, requestParameters.fieldSelector, requestParameters.sort, requestParameters.keyword, requestParameters.state, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @param {ApiFriendMoonyLaV1alpha1FriendPostApiParsingRssRequest} requestParameters Request parameters.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApi
     */
    public parsingRss(requestParameters: ApiFriendMoonyLaV1alpha1FriendPostApiParsingRssRequest, options?: RawAxiosRequestConfig) {
        return ApiFriendMoonyLaV1alpha1FriendPostApiFp(this.configuration).parsingRss(requestParameters.rssUrl, requestParameters.fetchLimitNumber, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @param {ApiFriendMoonyLaV1alpha1FriendPostApiSyncRssFeedRequest} requestParameters Request parameters.
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ApiFriendMoonyLaV1alpha1FriendPostApi
     */
    public syncRssFeed(requestParameters: ApiFriendMoonyLaV1alpha1FriendPostApiSyncRssFeedRequest, options?: RawAxiosRequestConfig) {
        return ApiFriendMoonyLaV1alpha1FriendPostApiFp(this.configuration).syncRssFeed(requestParameters.name, options).then((request) => request(this.axios, this.basePath));
    }
}

