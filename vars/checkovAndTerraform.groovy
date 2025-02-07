
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
    
//     # Run Checkov with custom policies only
//     echo "Running Checkov with custom policies only"
//     venv/bin/checkov -d ${WORKSPACE}/jenkins-shared-library/custom_policies -f plan.json --check CUSTOM_POLICY_001 || (echo "Checkov failed" && exit 1)
//     '''
// }


////
def installCheckov() {
    sh '''
    echo "Starting Checkov installation steps"
    pwd
    ls -la
    
    # Create a virtual environment
    python3 -m venv venv
    
    # Activate the virtual environment
    . venv/bin/activate
    
    # Install Checkov
    echo "Installing Checkov"
    venv/bin/pip install checkov
    
    # Verify Checkov installation
    if venv/bin/checkov --version; then
        echo "Checkov is installed"
    else
        echo "Checkov is not installed. Exiting."
        exit 1
    fi
    '''
}

def runCheckovAndTerraformPlan() {
    sh '''
    echo "Running Terraform and Checkov steps"
    
    # Activate the virtual environment
    . venv/bin/activate
    
    # Set the Terraform binary path
    TERRAFORM_BIN=/var/jenkins_home/bin/terraform

    # Verify Terraform installation
    if [ -x "$TERRAFORM_BIN" ]; then
        echo "Terraform is installed at $TERRAFORM_BIN"
    else
        echo "Terraform is not installed. Exiting."
        exit 1
    fi

    # Change to the directory containing Terraform configuration files
    cd ${WORKSPACE}
    
    # Initialize Terraform
    echo "Initializing Terraform"
    $TERRAFORM_BIN init
    
    # Plan Terraform deployment
    echo "Creating Terraform plan"
    $TERRAFORM_BIN plan -out=plan.out
    
    # Convert the plan output to JSON
    echo "Converting plan to JSON"
    $TERRAFORM_BIN show -json plan.out > plan.json
    
    # Verify JSON content
    echo "JSON Output of Terraform Plan:"
    cat plan.json
    
    # Run Checkov with custom policies only
    echo "Running Checkov with custom policies only"
    venv/bin/checkov -d ${WORKSPACE}/jenkins-shared-library/custom_policies -f plan.json --check CUSTOM_POLICY_001 || (echo "Checkov failed" && exit 1)
    '''
}
