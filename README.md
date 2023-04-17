# Storing Proxy Revision in a File

### ***Purpose***
The purpose of this script is to create or update a JSON file that tracks revisions of API proxies.

### ***Usage***
This script can be used in a Jenkins pipeline or shell script to create or update a JSON file that tracks revisions of API proxies.

### ***Dependencies***
This script requires the following dependencies to be installed and available in the environment:

- Groovy

- Jenkins

### ***Inputs***
- **$WORKSPACE -** the workspace directory where the JSON file will be created/updated.

- **env.PROXYNAME -** the name of the proxy being tracked.

- **params.APIGEE_REVISION -** the revision number of the API proxy

### ***Outputs***
This script creates or updates a JSON file located in $WORKSPACE/proxy-revision.json. The JSON file will have a structure like this:


`{
  "proxyName": {
    "stable_proxy_revision": ["revision1", "revision2", ...],
    "unstable_proxy_revision": ["revision3", "revision4", ...]
  },
  ...
}`

- **proxyName:** The name of the API proxy being tracked.

- **stable_proxy_revision:** A list of revisions that are currently stable.

- **unstable_proxy_revision:** A list of revisions that are currently unstable.

### ***How it works***

The script first checks if the JSON file exists in the specified $WORKSPACE. If it does not exist, the script creates an empty JSON file. Then, the script reads the contents of the JSON file and parses it into a JSON object using the JsonSlurper class.

Next, the script extracts the name of the proxy and the revision number from the input parameters. It checks if the proxy exists in the JSON object. If it does not exist, the script creates a new entry for the proxy with the revision number added to the stable_proxy_revision list. If the proxy already exists, the script checks if the revision number is already in the stable_proxy_revision list. If it is not, the script adds the revision number to the stable_proxy_revision list.

Finally, the script uses the JsonBuilder class to convert the updated JSON object back into a JSON string and write it back to the JSON file in a pretty-printed format.

**NOTE:**  Use the failure and success groovy scripts in the **POST-BUILD ACTIONS** of your Jenkinsfile.