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


// May contain unused imports in some cases
// @ts-ignore
import type { Link } from './link';
// May contain unused imports in some cases
// @ts-ignore
import type { RssFeedSyncLog } from './rss-feed-sync-log';

/**
 * A chunk of items.
 * @export
 * @interface ListedRssSyncLog
 */
export interface ListedRssSyncLog {
    /**
     * 
     * @type {Link}
     * @memberof ListedRssSyncLog
     */
    'link': Link;
    /**
     * 
     * @type {RssFeedSyncLog}
     * @memberof ListedRssSyncLog
     */
    'log'?: RssFeedSyncLog;
}

