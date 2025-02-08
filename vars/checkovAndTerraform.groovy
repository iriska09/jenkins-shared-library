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

//     // Set Correct Path for Custom Policies
//     def CUSTOM_POLICIES_DIR = "/var/jenkins_home/workspace/test-shared-libraries/jenkins-shared-library/custom_policies"
//     def PLAN_FILE = "/var/jenkins_home/workspace/test-shared-libraries/plan2.json"  // Define the plan file explicitly

//     // **Ensure Directory Exists for Custom Policies**
//     sh """
//     echo "Current WORKSPACE: ${WORKSPACE}"
//     echo "Checking for custom policies at: ${CUSTOM_POLICIES_DIR}"

//     # Ensure Custom Policies Directory Exists
//     if [ ! -d "${CUSTOM_POLICIES_DIR}" ]; then
//         echo "ERROR: Custom policies directory NOT found!"
//         exit 1
//     fi

//     # List the contents of the custom policies directory
//     echo "Listing contents of custom policies directory:"
//     ls -la ${CUSTOM_POLICIES_DIR}

//     # Ensure the plan2.json file exists
//     if [ ! -f "${PLAN_FILE}" ]; then
//         echo "ERROR: Plan file plan2.json not found!"
//         exit 1
//     fi
//     echo "Plan file ${PLAN_FILE} found."
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
//     /var/jenkins_home/bin/terraform show -json plan.out > plan2.json || { echo "Failed to convert plan to JSON. Exiting."; exit 1; }

//     # Ensure the plan2.json file exists
//     if [ ! -f "plan2.json" ]; then
//         echo "ERROR: Plan file plan2.json not found!"
//         exit 1
//     fi

//     echo "Plan file plan2.json found."

//     # **Running Checkov**
//     echo "Running Checkov with Custom Policies"

//     # Debugging the Checkov command to ensure it's correct
//     echo "Running Checkov command:"
//     echo "checkov -d /var/jenkins_home/workspace/test-shared-libraries/ -f plan2.json --external-checks-dir=${CUSTOM_POLICIES_DIR} --debug"

//     # Run Checkov with the plan file and custom policies
//     checkov -d /var/jenkins_home/workspace/test-shared-libraries/ -f plan2.json --external-checks-dir=${CUSTOM_POLICIES_DIR} --debug || { echo "Checkov failed! Exiting."; exit 1; }
//     '''
// }

def runCheckovAndTerraformPlan() {
    echo "=== Running Terraform and Checkov ==="

    // Set Correct Path for Custom Policies
    def CUSTOM_POLICIES_DIR = "/var/jenkins_home/workspace/test-shared-libraries/jenkins-shared-library/custom_policies"
    def PLAN_FILE = "/var/jenkins_home/workspace/test-shared-libraries/plan2.json"  // Define the plan file explicitly

    // **Ensure Directory Exists for Custom Policies**
    sh """
    echo "Current WORKSPACE: ${WORKSPACE}"
    echo "Checking for custom policies at: ${CUSTOM_POLICIES_DIR}"

    # Ensure Custom Policies Directory Exists
    if [ ! -d "${CUSTOM_POLICIES_DIR}" ]; then
        echo "ERROR: Custom policies directory NOT found!"
        exit 1
    fi

    # List the contents of the custom policies directory
    echo "Listing contents of custom policies directory:"
    ls -la ${CUSTOM_POLICIES_DIR}

    # Ensure the plan2.json file exists
    if [ ! -f "${PLAN_FILE}" ]; then
        echo "ERROR: Plan file plan2.json not found!"
        exit 1
    fi
    echo "Plan file ${PLAN_FILE} found."
    """

    sh """
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
    /var/jenkins_home/bin/terraform show -json plan.out > plan2.json || { echo "Failed to convert plan to JSON. Exiting."; exit 1; }

    # Ensure the plan2.json file exists
    if [ ! -f "plan2.json" ]; then
        echo "ERROR: Plan file plan2.json not found!"
        exit 1
    fi

    echo "Plan file plan2.json found."

    # **Running Checkov**
    echo "Running Checkov with Custom Policies"

    # Run Checkov with the plan file and custom policies
    checkov -d /var/jenkins_home/workspace/test-shared-libraries/ -f plan2.json --external-checks-dir="${CUSTOM_POLICIES_DIR}" --debug || { echo "Checkov failed! Exiting."; exit 1; }
    """
}
