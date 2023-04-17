def jsonfile = new File("$WORKSPACE/proxy-revision.json")
if(jsonfile.exists() == false){
    jsonfile.write("{}")
}
def jsonPayload = jsonfile.text
def slurper = new groovy.json.JsonSlurper()
def json = slurper.parseText(jsonPayload)
def builder = new groovy.json.JsonBuilder()

String field = "${env.PROXYNAME}"
String revision = "${params.APIGEE_REVISION}"

proxy_exists = json.keySet().contains(field)


if (proxy_exists==false){
    String[] proxy_revision = new String[1]
    String[] success_proxy_revision = new String[0]
    proxy_revision[0] = revision


    def newproxy_element = [
        'stable_proxy_revision' : success_proxy_revision,
        'unstable_proxy_revision' : proxy_revision
    ]

    json."${field}" = newproxy_element
    def json_builder = new groovy.json.JsonBuilder(json)
    File file = new File("$WORKSPACE/proxy-revision.json")
    file.write(json_builder.toPrettyString())
}
else{
    String[] proxy_revision_list = json."${field}"."unstable_proxy_revision"
    String[] modified_proxy_revision_list = new String[proxy_revision_list.size()+1]
    
    int i = 0
    for(i = 0; i < proxy_revision_list.size(); i++){
      modified_proxy_revision_list[i] = proxy_revision_list[i]
    }
    modified_proxy_revision_list[i] = revision
    
    json."${field}"."unstable_proxy_revision" = modified_proxy_revision_list
    
    def json_builder = new groovy.json.JsonBuilder(json)
    
    File file = new File("$WORKSPACE/proxy-revision.json")
    file.write(json_builder.toPrettyString()) 
}