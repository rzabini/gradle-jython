/*
 * Copyright 2015 the original author or authors.
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

package jython.util

import spock.lang.Specification


class PackageFinderSpec extends Specification{

    def "can find an existing package archive"(){

        when:
        URL url=PackageFinder.findPackageArchive(name, version)
        then:
        url.file.endsWith("$name-${version}.tar.gz")
        where:
        name        |version
        'Pygments'  |'2.0.2'
        'Sphinx'    |'1.3.1'
    }
}
