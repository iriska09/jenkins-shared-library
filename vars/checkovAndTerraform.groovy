// def runCheckovAndTerraform() {
//     def checkovPassed = false
//     try {
//         sh '''
//         echo "Starting Checkov and Terraform steps"
        
//         # Create a virtual environment
//         python3 -m venv venv
        
//         # Activate the virtual environment
//         . venv/bin/activate
        
//         # Install Checkov
//         echo "Installing Checkov"
//         venv/bin/pip install checkov
        
//         # Verify Checkov installation
//         if venv/bin/checkov --version; then
//             echo "Checkov is installed"
//         else
//             echo "Checkov is not installed. Exiting."
//             exit 1
//         fi
        
//         # Download and install Terraform
//         echo "Downloading Terraform"
//         wget https://releases.hashicorp.com/terraform/1.0.11/terraform_1.0.11_linux_amd64.zip
//         unzip -o terraform_1.0.11_linux_amd64.zip
//         mkdir -p ${HOME}/bin
//         mv terraform ${HOME}/bin/
        
//         # Add ${HOME}/bin to PATH
//         export PATH=${HOME}/bin:$PATH
        
//         # Verify Terraform installation
//         if terraform --version; then
//             echo "Terraform is installed"
//         else
//             echo "Terraform is not installed. Exiting."
//             exit 1
//         fi
        
//         # Initialize Terraform
//         echo "Initializing Terraform"
//         terraform init
        
//         # Plan Terraform deployment
//         echo "Creating Terraform plan"
//         terraform plan -out=plan.out
        
//         # Convert the plan output to JSON
//         echo "Converting plan to JSON"
//         terraform show -json plan.out > plan.json
        
//         # Run Checkov
//         echo "Running Checkov"
//         venv/bin/checkov -f plan.json || (echo "Checkov failed" && exit 1)
        
//         # Deactivate and remove the virtual environment
//         deactivate
//         rm -rf venv
//         '''
//         checkovPassed = true
//     } catch (Exception e) {
//         echo "Checkov failed: ${e.message}"
//     }
//     return checkovPassed
// }
def installCheckov() {
    sh '''
    echo "Starting Checkov installation steps"

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
    
    # Set the Terraform binary path (update to the correct path found earlier)
    export TERRAFORM_BIN=/usr/local/bin/terraform
    
    # Verify Terraform installation
    if $TERRAFORM_BIN --version; then
        echo "Terraform is installed"
    else
        echo "Terraform is not installed. Exiting."
        exit 1
    fi
    
    # Initialize Terraform
    echo "Initializing Terraform"
    $TERRAFORM_BIN init
    
    # Plan Terraform deployment
    echo "Creating Terraform plan"
    $TERRAFORM_BIN plan -out=plan.out
    
    # Convert the plan output to JSON
    echo "Converting plan to JSON"
    $TERRAFORM_BIN show -json plan.out > plan.json
    
    # Run Checkov
    echo "Running Checkov"
    venv/bin/checkov -f plan.json || (echo "Checkov failed" && exit 1)
    '''
}
