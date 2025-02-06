// vars/checkovAndTerraform.groovy
def runCheckov() {
    def checkovPassed = false
    try {
        sh '''
        # Install Checkov if not already installed
        if ! checkov --version; then
            pip install checkov
        fi
        
        # Run Checkov with custom policies
        checkov -d . --policy-directory custom_policies
        '''
        checkovPassed = true
    } catch (Exception e) {
        echo "Checkov failed: ${e.message}"
    }
    return checkovPassed
}

def runTerraformCommands() {
    sh '''
    # Initialize Terraform
    terraform init
    
    # Plan Terraform deployment
    terraform plan -out=plan.out
    
    # Convert the plan output to JSON
    terraform show -json plan.out > plan.json
    '''
}
