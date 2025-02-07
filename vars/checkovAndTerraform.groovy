// def installCheckov() {
//     sh '''
//     echo "Starting Checkov installation steps"
//     pwd
//     ls -la
    
//     # Create a virtual environment
//     python3 -m venv venv
    
//     # Activate the virtual environment
//     . venv/bin/activate
    
//     # Install Checkov
//     echo "Installing Checkov"
//     venv/bin/pip install checkov
    
//     # Verify Checkov installation
//     if venv/bin/checkov --version; then
//         echo "Checkov is installed"
//     else
//         echo "Checkov is not installed. Exiting."
//         exit 1
//     fi
//     '''
// }

// def runCheckovAndTerraformPlan() {
//     sh '''
//     echo "Running Terraform and Checkov steps"
    
//     # Activate the virtual environment
//     . venv/bin/activate
    
//     # Set the Terraform binary path
//     TERRAFORM_BIN=/var/jenkins_home/bin/terraform

//     # Verify Terraform installation
//     if [ -x "$TERRAFORM_BIN" ]; then
//         echo "Terraform is installed at $TERRAFORM_BIN"
//     else
//         echo "Terraform is not installed. Exiting."
//         exit 1
//     fi

//     # Change to the directory containing Terraform configuration files
//     cd ${WORKSPACE}
    
//     # Initialize Terraform
//     echo "Initializing Terraform"
//     $TERRAFORM_BIN init
    
//     # Plan Terraform deployment
//     echo "Creating Terraform plan"
//     $TERRAFORM_BIN plan -out=plan.out
    
//     # Convert the plan output to JSON
//     echo "Converting plan to JSON"
//     $TERRAFORM_BIN show -json plan.out > plan.json
    
//     # Verify JSON content
//     echo "JSON Output of Terraform Plan:"
//     cat plan.json
    
//     # Run Checkov with custom policies only
//     echo "Running Checkov with custom policies only"
//     venv/bin/checkov -d ${WORKSPACE}/jenkins-shared-library/custom_policies -f plan.json --check CUSTOM_POLICY_001 || (echo "Checkov failed" && exit 1)
//     '''
// }

/// 2 CP Code 
// checkovAndTerraform.groovy

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
    
    echo "=== Checkov Installed Successfully ==="
    '''
}

def runCheckovAndTerraformPlan() {
    echo "=== Running Terraform and Checkov ==="
    sh '''
    # Activate the virtual environment
    . venv/bin/activate || { echo "Failed to activate virtual environment! Exiting."; exit 1; }
    
    # Set the Terraform binary path
    TERRAFORM_BIN=/var/jenkins_home/bin/terraform

    # Verify Terraform installation
    if [ ! -x "$TERRAFORM_BIN" ]; then
        echo "Terraform is not installed or not executable. Exiting."
        exit 1
    fi

    # Initialize Terraform
    echo "Initializing Terraform"
    $TERRAFORM_BIN init || { echo "Terraform init failed. Exiting."; exit 1; }
    
    # Create the Terraform plan
    echo "Creating Terraform plan"
    $TERRAFORM_BIN plan -out=plan.out || { echo "Terraform plan failed. Exiting."; exit 1; }
    
    # Convert the plan to JSON
    echo "Converting plan to JSON"
    $TERRAFORM_BIN show -json plan.out > plan.json || { echo "Failed to convert plan to JSON. Exiting."; exit 1; }
    
    # Run Checkov with the generated plan and custom policies
    # Make sure to use your custom policies and optionally skip the AWS default policies
    checkov -d ${WORKSPACE}/jenkins-shared-library/custom_policies -f plan.json --skip-check CKV_AWS_135 --skip-check CKV_AWS_126 --skip-check CKV_AWS_88 || { echo "Checkov failed. Exiting."; exit 1; }
    '''
}


/// CGP
// // Function to install Checkov
// def installCheckov() {
//     sh '''
//     echo "Starting Checkov installation steps"
    
//     # Create a virtual environment if it doesn't exist
//     if [ ! -d "venv" ]; then
//         python3 -m venv venv
//         echo "Created virtual environment"
//     else
//         echo "Virtual environment already exists"
//     fi
    
//     # Activate the virtual environment
//     source venv/bin/activate
    
//     # Install Checkov if not already installed
//     if ! pip show checkov &>/dev/null; then
//         echo "Installing Checkov..."
//         pip install checkov
//     else
//         echo "Checkov is already installed"
//     fi
    
//     # Verify Checkov installation
//     if ! checkov --version; then
//         echo "Checkov installation failed. Exiting."
//         exit 1
//     fi

//     echo "Checkov installation completed successfully"
//     '''
// }

// // Function to run Terraform and Checkov plan
// def runCheckovAndTerraformPlan() {
//     sh '''
//     echo "Running Terraform and Checkov steps"
    
//     # Activate the virtual environment
//     source venv/bin/activate

//     # Verify Terraform installation
//     if ! command -v terraform &>/dev/null; then
//         echo "Terraform is not installed. Exiting."
//         exit 1
//     fi
    
//     # Initialize Terraform
//     terraform init || { echo "Terraform init failed. Exiting."; exit 1; }
    
//     # Generate Terraform plan
//     terraform plan -out=plan.out || { echo "Terraform plan failed. Exiting."; exit 1; }
    
//     # Convert plan to JSON
//     terraform show -json plan.out > plan.json || { echo "Terraform show failed. Exiting."; exit 1; }
    
//     # Check if plan.json is generated
//     if [ ! -s plan.json ]; then
//         echo "Plan JSON file is empty. Exiting."
//         exit 1
//     fi
    
//     echo "Plan JSON generated successfully"
    
//     # Run Checkov with custom policies
//     checkov -d ${WORKSPACE}/jenkins-shared-library/custom_policies -f plan.json --check CUSTOM_POLICY_001 --check CUSTOM_POLICY_002 --check CUSTOM_POLICY_003
//     if [ $? -ne 0 ]; then
//         echo "Checkov failed. Exiting."
//         exit 1
//     fi
    
//     echo "Checkov passed successfully"
//     '''
// }
