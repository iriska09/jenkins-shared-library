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
//     if ! $TERRAFORM_BIN init; then
//         echo "Terraform init failed. Exiting."
//         exit 1
//     fi
    
//     # Plan Terraform deployment
//     echo "Creating Terraform plan"
//     if ! $TERRAFORM_BIN plan -out=plan.out; then
//         echo "Terraform plan failed. Exiting."
//         exit 1
//     fi
    
//     # Convert the plan output to JSON
//     echo "Converting plan to JSON"
//     if ! $TERRAFORM_BIN show -json plan.out > plan.json; then
//         echo "Terraform show failed. Exiting."
//         exit 1
//     fi
    
//     # Verify JSON content
//     echo "JSON Output of Terraform Plan:"
//     cat plan.json
    
//     # Run Checkov with custom policies only
//     echo "Running Checkov with custom policies only"
//     venv/bin/checkov -d ${WORKSPACE}/jenkins-shared-library/custom_policies -f plan.json --check CUSTOM_POLICY_001 --check CUSTOM_POLICY_002 --check CUSTOM_POLICY_003 || (echo "Checkov failed" && exit 1)
//     '''
// }


///// CGP
def installCheckov() {
    sh '''
    set -x  # Enable debugging for all commands
    echo "===== Starting Checkov Installation ====="

    # Check if Python is installed
    if ! command -v python3 &> /dev/null
    then
        echo "Python3 is not installed. Installing Python3..."
        apt-get update && apt-get install -y python3 python3-pip python3-venv || exit 1
    else
        echo "Python3 is already installed."
    fi

    # Create a fresh virtual environment if it doesn't exist
    if [ ! -d "venv" ]; then
        echo "Creating virtual environment..."
        python3 -m venv venv || exit 1
    else
        echo "Virtual environment already exists."
    fi

    # Activate the virtual environment
    echo "Activating virtual environment..."
    . venv/bin/activate || exit 1

    # Install Checkov if it's not already installed
    if ! pip show checkov &> /dev/null; then
        echo "Installing Checkov..."
        pip install checkov || exit 1
    else
        echo "Checkov is already installed."
    fi

    # Verify Checkov installation
    checkov --version || { echo "Checkov verification failed"; exit 1; }

    echo "===== Checkov Installation Completed Successfully ====="
    set +x  # Disable debugging
    '''
}

def runCheckovAndTerraform() {
    sh '''
    set -x  # Enable debugging for all commands
    echo "===== Running Checkov Steps ====="

    # Activate the virtual environment
    echo "Activating virtual environment..."
    . venv/bin/activate || exit 1

    # Set the Terraform binary path (adjust as needed)
    TERRAFORM_BIN=/var/jenkins_home/bin/terraform

    # Verify Terraform installation
    if [ -x "$TERRAFORM_BIN" ]; then
        echo "Terraform is installed at $TERRAFORM_BIN"
    else
        echo "Terraform is not installed. Exiting."
        exit 1
    fi

    # Initialize Terraform
    echo "Initializing Terraform..."
    if ! $TERRAFORM_BIN init; then
        echo "Terraform init failed. Exiting."
        exit 1
    fi

    # Create Terraform plan
    echo "Creating Terraform plan..."
    if ! $TERRAFORM_BIN plan -out=plan.out; then
        echo "Terraform plan failed. Exiting."
        exit 1
    fi

    # Convert the plan to JSON
    echo "Converting Terraform plan to JSON..."
    if ! $TERRAFORM_BIN show -json plan.out > plan.json; then
        echo "Terraform show failed. Exiting."
        exit 1
    fi

    # Run Checkov with custom policies only
    echo "Running Checkov with custom policies only..."
    checkov -d ${WORKSPACE}/jenkins-shared-library/custom_policies -f plan.json --check CUSTOM_POLICY_001 --check CUSTOM_POLICY_002 --check CUSTOM_POLICY_003

    if [ $? -ne 0 ]; then  # If Checkov fails (non-zero exit code)
        echo "Checkov failed. Exiting."
        exit 1
    fi

    echo "===== Checkov Passed Successfully ====="
    set +x  # Disable debugging
    '''
}
