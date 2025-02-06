def runCheckovAndTerraform() {
    def checkovPassed = false
    try {
        sh '''
        echo "Starting Checkov and Terraform steps"

        # Check if Checkov is installed
        if checkov --version; then
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

        # Run Checkov (custom policies not required for now)
        echo "Running Checkov"
        checkov -f plan.json || (echo "Checkov failed" && exit 1)
        '''
        checkovPassed = true
    } catch (Exception e) {
        echo "Checkov failed: ${e.message}"
    }
    return checkovPassed
}

