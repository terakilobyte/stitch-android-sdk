/*
 * Copyright 2018-present MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mongodb.stitch.core;

/**
 * StitchClientErrorCode represents the errors that can occur when using the Stitch client,
 * typically before a request is made to the Stitch server.
 */
public enum StitchClientErrorCode {
  LOGGED_OUT_DURING_REQUEST,
  MISSING_URL,
  MUST_AUTHENTICATE_FIRST,
  USER_NO_LONGER_VALID,
  COULD_NOT_LOAD_PERSISTED_AUTH_INFO,
  COULD_NOT_PERSIST_AUTH_INFO
}
