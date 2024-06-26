/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iabtechlab.openrtb.v2.snippet;

/**
 * Macros for snippet generation. Enum types will implement this interface and provide
 * groups of macros supported by some implementation of {@link SnippetProcessor}.
 */
public interface SnippetMacroType {

  /**
   * Returns the macro key (value that will be substituted).
   */
  String key();
}
