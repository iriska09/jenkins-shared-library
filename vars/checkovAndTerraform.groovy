def runCheckovAndTerraform() {
    def checkovPassed = false
    try {
        sh '''
        echo "Starting Checkov and Terraform steps"
        
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
        
        # Initialize Terraform
        echo "Initializing Terraform"
        terraform init
        
        # Plan Terraform deployment
        echo "Creating Terraform plan"
        terraform plan -out=plan.out
        
        # Convert the plan output to JSON
        echo "Converting plan to JSON"
        terraform show -json plan.out > plan.json
        
        # Run Checkov
        echo "Running Checkov"
        venv/bin/checkov -f plan.json || (echo "Checkov failed" && exit 1)
        
        # Deactivate and remove the virtual environment
        deactivate
        rm -rf venv
        '''
        checkovPassed = true
    } catch (Exception e) {
        echo "Checkov failed: ${e.message}"
    }
    return checkovPassed
}
