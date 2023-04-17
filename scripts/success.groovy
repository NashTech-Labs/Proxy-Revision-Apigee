def jsonfile = new File("$WORKSPACE/proxy-revision.json")
        if (jsonfile.exists() == false) {
            jsonfile.write("{}")
        }

        def jsonPayload = sh(script: "cat $WORKSPACE/proxy-revision.json", returnStdout: true).trim()
        def json = jsonPayload.isEmpty() ? [:] : new groovy.json.JsonSlurper().parseText(jsonPayload)
        def builder = new groovy.json.JsonBuilder()

        String field = "${env.PROXYNAME}"
        String revision = "${params.APIGEE_REVISION}"
        println field
        println revision

        def proxyElement = json[field]
        if (proxyElement == null) {
            String[] proxy_revision = new String[1]
            String[] failed_proxy_revision = new String[0]
            proxy_revision[0] = revision
            def newproxy_element = [
                'stable_proxy_revision': proxy_revision,
                'unstable_proxy_revision': failed_proxy_revision
            ]
            json."${field}" = newproxy_element
        } else {
            println "proxy exists appending to the file"
            String[] proxy_revision_list = proxyElement."stable_proxy_revision"
            if (!proxy_revision_list.contains(revision)) {
                String[] modified_proxy_revision_list = new String[proxy_revision_list.size() + 1]
                int i = 0
                for (i = 0; i < proxy_revision_list.size(); i++) {
                    modified_proxy_revision_list[i] = proxy_revision_list[i]
                }
                modified_proxy_revision_list[i] = revision
                proxyElement."stable_proxy_revision" = modified_proxy_revision_list
            }
        }

        def json_builder = new groovy.json.JsonBuilder(json)
        File file = new File("$WORKSPACE/proxy-revision.json")
        file.write(json_builder.toPrettyString())