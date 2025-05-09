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
import type { CronFriendPost } from './cron-friend-post';

/**
 * 
 * @export
 * @interface CronFriendPostList
 */
export interface CronFriendPostList {
    /**
     * Indicates whether current page is the first page.
     * @type {boolean}
     * @memberof CronFriendPostList
     */
    'first': boolean;
    /**
     * Indicates whether current page has previous page.
     * @type {boolean}
     * @memberof CronFriendPostList
     */
    'hasNext': boolean;
    /**
     * Indicates whether current page has previous page.
     * @type {boolean}
     * @memberof CronFriendPostList
     */
    'hasPrevious': boolean;
    /**
     * A chunk of items.
     * @type {Array<CronFriendPost>}
     * @memberof CronFriendPostList
     */
    'items': Array<CronFriendPost>;
    /**
     * Indicates whether current page is the last page.
     * @type {boolean}
     * @memberof CronFriendPostList
     */
    'last': boolean;
    /**
     * Page number, starts from 1. If not set or equal to 0, it means no pagination.
     * @type {number}
     * @memberof CronFriendPostList
     */
    'page': number;
    /**
     * Size of each page. If not set or equal to 0, it means no pagination.
     * @type {number}
     * @memberof CronFriendPostList
     */
    'size': number;
    /**
     * Total elements.
     * @type {number}
     * @memberof CronFriendPostList
     */
    'total': number;
    /**
     * Indicates total pages.
     * @type {number}
     * @memberof CronFriendPostList
     */
    'totalPages': number;
}

