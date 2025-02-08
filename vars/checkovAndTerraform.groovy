def installCheckov() {
    echo "=== Starting Checkov Installation ==="
    sh '''
    # Check Python3 installation
    which python3 || { echo "Python3 is not installed! Exiting."; exit 1; }
    
    # Create a virtual environment
    python3 -m venv venv || { echo "Failed to create virtual environment! Exiting."; exit 1; }
    
    # Activate the virtual environment
    . venv/bin/activate || { echo "Failed to activate virtual environment! Exiting."; exit 1; }
    
    # Install Checkov
    pip install checkov || { echo "Failed to install Checkov! Exiting."; exit 1; }
    
    # Verify Checkov installation
    checkov --version || { echo "Checkov verification failed! Exiting."; exit 1; }
    which checkov
    
    echo "=== Checkov Installed Successfully ==="
    '''
}
// def runCheckovAndTerraformPlan() {
//     echo "=== Running Terraform and Checkov ==="
    
//     // **Set Correct Path for Custom Policies**
//     def customPoliciesPath = "/var/jenkins_home/workspace/test-shared-libraries@2/jenkins-shared-library/custom_policies"

//     sh """
//     echo "Current WORKSPACE: ${WORKSPACE}"
//     echo "Checking for custom policies at: ${customPoliciesPath}"

//     # **Ensure Directory Exists**
//     if [ ! -d "${customPoliciesPath}" ]; then
//         echo "ERROR: Custom policies directory NOT found!"
//         exit 1
//     fi
//     """

//     sh '''
//     # **Activate Virtual Environment Properly**
//     if [ -f "venv/bin/activate" ]; then
//         . venv/bin/activate
//     else
//         echo "Virtual environment not found! Exiting."
//         exit 1
//     fi

//     # **Terraform Plan Execution**
//     echo "Creating Terraform plan"
//     /var/jenkins_home/bin/terraform plan -out=plan.out || { echo "Terraform plan failed. Exiting."; exit 1; }
    
//     echo "Converting plan to JSON"
//     /var/jenkins_home/bin/terraform show -json plan.out > plan.json || { echo "Failed to convert plan to JSON. Exiting."; exit 1; }

//     echo "Running Checkov with Custom Policies"
    
//     # **Corrected Checkov Execution with the Right Path**
//     checkov -d . -f plan.json --external-checks-dir="${customPoliciesPath}" --debug || { echo "Checkov failed. Exiting."; exit 1; }
//     '''
// }
def runCheckovAndTerraformPlan() {
    echo "=== Running Terraform and Checkov ==="

    // **Set Correct Path for Custom Policies**
    def customPoliciesPath = "/var/jenkins_home/workspace/test-shared-libraries@2/jenkins-shared-library/custom_policies"

    sh """
    echo "Current WORKSPACE: ${WORKSPACE}"
    echo "Checking for custom policies at: ${customPoliciesPath}"

    # **Ensure Directory Exists**
    if [ ! -d "${customPoliciesPath}" ]; then
        echo "ERROR: Custom policies directory NOT found!"
        exit 1
    fi

    # **List the contents of the custom policies directory**
    echo "Listing contents of custom policies directory:"
    ls -la ${customPoliciesPath}
    """

    sh '''
    # **Activate Virtual Environment Properly**
    if [ -f "venv/bin/activate" ]; then
        . venv/bin/activate
    else
        echo "Virtual environment not found! Exiting."
        exit 1
    fi

    # **Terraform Plan Execution**
    echo "Creating Terraform plan"
    /var/jenkins_home/bin/terraform plan -out=plan.out || { echo "Terraform plan failed. Exiting."; exit 1; }
    
    echo "Converting plan to JSON"
    /var/jenkins_home/bin/terraform show -json plan.out > plan.json || { echo "Failed to convert plan to JSON. Exiting."; exit 1; }

    echo "Running Checkov with Custom Policies"
    
    # **Corrected Checkov Execution with the Right Path**
   checkov -d /var/jenkins_home/workspace/test-shared-libraries --f plan.json --external-checks-dir=${CUSTOM_POLICIES_DIR} --debug || { echo "Checkov failed! Exiting."; exit 1; }

    '''
}
