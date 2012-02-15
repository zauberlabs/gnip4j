## What is gnip4j?

Gnip4j is an Open Source (ASFL 2.0) library to access and process Activities (tweets)
from the [Gnip API](http://docs.gnip.com/w/page/23724581/Gnip-Full-Documentation)
from the data from the Java language.


## Features
   * 100% Pure Java - works on any Java Platform version 1.6 or later 
   * Minimal dependencies: 
      * The [Jackson JSON library](http://jackson.codehaus.org/) is the only requiered dependecy
      * Optionally if `slf4-api` is present in the classpath, logging is supported.
      * Optionally [Apache HttpClient](http://hc.apache.org/httpcomponents-client-ga/) is supported. 
   * Out-of-the-box gzip support
   * Push model via Streaming (asynchronous processing)
   * Error handling support (and exponential-backoff reconnections)
   * Monitoring Support (JMX)
   * [Mule ESB](http://www.mulesoft.org/) integration

## How To Contribute

[Send pull requests](http://help.github.com/pull-requests/) using GitHub.

Had an Issue? Fill it in https://github.com/zauberlabs/gnip4j/issues .

## Who is using gnip4j?

Gnip4j was developed by [Zauber](http://www.zaubersoftware.com/), a software boutique & lab based
in Buenos Aires, Argentina. We built Gnip4j when we started using Gnip Twitter Premium Feeds. 
We use their service to create scalable social media dashboards and interactive visualizations that
operate in real time. We were already using Twitter4j for other Twitter-related development projects, 
thus we thought that building a sister project Gnip4j would make perfect sense. 
We hope you find it useful too. [Contact us](http://www.zaubersoftware.com/en/contact/)
if you want to contribute to this project or just have any specific requests.

## License


Copyright (c) 2011 Zauber S.A. <http://www.zaubersoftware.com/>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

gnip4j includes portions of software from licenced by the Apache Fundation (package
`com.zaubersoftware.gnip4j.api.stats.commonsio`).

## Code Conventions and Housekeeping

None of these is essential for a pull request, but they will all help.  They
can also be added after the original pull
request but before a merge.

* Use the project code format conventions. Import `XXXX.xml` from the root of
  the project if you are using Eclipse. Checkstyle configuration is available 
  at `XXXX.xml`
* Make sure all new .java files to have a simple Javadoc class comment with at
  least an @author tag identifying you, and preferably at least a paragraph on 
  what the class is for.
* Add the ASF license header comment to all new .java files (copy from existing 
  files in the project)
* Add yourself as an @author to the .java files that you modify substantially
  (more than cosmetic changes).
* A few unit tests would help a lot as well - someone has to do it.
* If no-one else is using your branch, please rebase it against the current
  master (or other target branch in the main project).
