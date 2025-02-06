def runCheckovAndTerraform() {
    def checkovPassed = false
    try {
        sh '''
        # Install Checkov if not already installed
        if ! checkov --version; then
            pip install checkov
        fi
        
        # Initialize Terraform
        terraform init
        
        # Plan Terraform deployment
        terraform plan -out=plan.out
        
        # Convert the plan output to JSON
        terraform show -json plan.out > plan.json
        
        # Run Checkov (custom policies not required for now)
        checkov -f plan.json
        '''
        checkovPassed = true
    } catch (Exception e) {
        echo "Checkov failed: ${e.message}"
    }
    return checkovPassed
}
