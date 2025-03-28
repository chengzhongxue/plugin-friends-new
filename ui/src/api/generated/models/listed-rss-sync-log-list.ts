/* tslint:disable */
/* eslint-disable */
/**
 * Halo
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 2.19.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


// May contain unused imports in some cases
// @ts-ignore
import type {ListedRssSyncLog} from "@/api/generated";

/**
 * 
 * @export
 * @interface ListedRssSyncLogList
 */
export interface ListedRssSyncLogList {
    /**
     * Indicates whether current page is the first page.
     * @type {boolean}
     * @memberof ListedRssSyncLogList
     */
    'first': boolean;
    /**
     * Indicates whether current page has previous page.
     * @type {boolean}
     * @memberof ListedRssSyncLogList
     */
    'hasNext': boolean;
    /**
     * Indicates whether current page has previous page.
     * @type {boolean}
     * @memberof ListedRssSyncLogList
     */
    'hasPrevious': boolean;
    /**
     * A chunk of items.
     * @type {Array<ListedRssSyncLog>}
     * @memberof ListedRssSyncLogList
     */
    'items': Array<ListedRssSyncLog>;
    /**
     * Indicates whether current page is the last page.
     * @type {boolean}
     * @memberof ListedRssSyncLogList
     */
    'last': boolean;
    /**
     * Page number, starts from 1. If not set or equal to 0, it means no pagination.
     * @type {number}
     * @memberof ListedRssSyncLogList
     */
    'page': number;
    /**
     * Size of each page. If not set or equal to 0, it means no pagination.
     * @type {number}
     * @memberof ListedRssSyncLogList
     */
    'size': number;
    /**
     * Total elements.
     * @type {number}
     * @memberof ListedRssSyncLogList
     */
    'total': number;
    /**
     * Indicates total pages.
     * @type {number}
     * @memberof ListedRssSyncLogList
     */
    'totalPages': number;
}

