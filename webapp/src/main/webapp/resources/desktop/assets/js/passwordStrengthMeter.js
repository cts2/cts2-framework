// Password strength meter
// This jQuery plugin is written by firas kassem [2007.04.05]
// Firas Kassem  phiras.wordpress.com || phiras at gmail {dot} com
// for more information : http://phiras.wordpress.com/2007/04/08/password-strength-meter-a-jquery-plugin/

var shortPass = 'Too short'
var badPass = 'Weak'
var goodPass = 'Good'
var strongPass = 'Strong'

function initPasswordFields(oldPassword,getOldPasswordFunction,password,confirmPassword,oldPasswordResult,strengthResult,matchResult,submitButton){
	submitButton.button( "option", "disabled", true );
	
	password.keyup(
		function(){
			onPasswordKeyUp(oldPassword,getOldPasswordFunction,password,confirmPassword,oldPasswordResult,strengthResult,matchResult,submitButton);
		}
	);
	confirmPassword.keyup(
		function(){
			onPasswordKeyUp(oldPassword,getOldPasswordFunction,password,confirmPassword,oldPasswordResult,strengthResult,matchResult,submitButton);
		}
	);
}

function onPasswordKeyUp(oldPassword,getOldPasswordFunction,password,confirmPassword,oldPasswordResult,strengthResult,matchResult,submitButton){
	var oldPasswordMatch = checkOldPassword(oldPassword,getOldPasswordFunction,oldPasswordResult);
	var strength = checkPasswordStrength(password,strengthResult);
	var match = checkForPasswordMatch(password,confirmPassword, matchResult);
	
	if(match && strength && oldPasswordMatch){
		submitButton.button( "option", "disabled", false );
	} else {
		submitButton.button( "option", "disabled", true );
	}
}

function checkOldPassword(password,getOldPasswordFunction,result){
	var pass = password.val();
	
	var ok = pass == getOldPasswordFunction();
	
	if(ok){
		result.html("Match");
		result.css('color', 'green');
	} else {
		result.html("Mismatch");
		result.css('color', 'red');
	}
	
	return ok;
}

function checkPasswordStrength(password,result){
	var resultText = passwordStrength(password.val());
	result.html(resultText);
	
	var color = "black";
	var ok = true;
	
	switch(resultText){
		case shortPass:
			color = "black";
			ok = false;
			break;
		case badPass:
			color = "red";
			break;
		case goodPass:
			color = "yellow";
			break;
		case strongPass:
			color = "green";
			break;
	}
	
	result.css('color', color);
	
	return ok;
}

function checkForPasswordMatch(field1, field2, result){
	var newPassword = field1.val();
	var verifyPassword = field2.val();
	
	var response;
	var ok;
	
	if(newPassword == verifyPassword){
		response = "Match";
		ok = true;
	} else {
		response = "No Match";
		ok = false;
	}

	$(result).html(response);
	
	return ok;
}

function passwordStrength(password)
{
    score = 0 
    
    //password < 4
    if (password.length < 4 ) { return shortPass }
    
   
    //password length
    score += password.length * 4
    score += ( checkRepetition(1,password).length - password.length ) * 1
    score += ( checkRepetition(2,password).length - password.length ) * 1
    score += ( checkRepetition(3,password).length - password.length ) * 1
    score += ( checkRepetition(4,password).length - password.length ) * 1

    //password has 3 numbers
    if (password.match(/(.*[0-9].*[0-9].*[0-9])/))  score += 5 
    
    //password has 2 sybols
    if (password.match(/(.*[!,@,#,$,%,^,&,*,?,_,~].*[!,@,#,$,%,^,&,*,?,_,~])/)) score += 5 
    
    //password has Upper and Lower chars
    if (password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/))  score += 10 
    
    //password has number and chars
    if (password.match(/([a-zA-Z])/) && password.match(/([0-9])/))  score += 15 
    //
    //password has number and symbol
    if (password.match(/([!,@,#,$,%,^,&,*,?,_,~])/) && password.match(/([0-9])/))  score += 15 
    
    //password has char and symbol
    if (password.match(/([!,@,#,$,%,^,&,*,?,_,~])/) && password.match(/([a-zA-Z])/))  score += 15 
    
    //password is just a nubers or chars
    if (password.match(/^\w+$/) || password.match(/^\d+$/) )  score -= 10 
    
    //verifing 0 < score < 100
    if ( score < 0 )  score = 0 
    if ( score > 100 )  score = 100 
    
    if (score < 34 )  return badPass 
    if (score < 68 )  return goodPass
    return strongPass
}


// checkRepetition(1,'aaaaaaabcbc')   = 'abcbc'
// checkRepetition(2,'aaaaaaabcbc')   = 'aabc'
// checkRepetition(2,'aaaaaaabcdbcd') = 'aabcd'

function checkRepetition(pLen,str) {
    res = ""
    for ( i=0; i<str.length ; i++ ) {
        repeated=true
        for (j=0;j < pLen && (j+i+pLen) < str.length;j++)
            repeated=repeated && (str.charAt(j+i)==str.charAt(j+i+pLen))
        if (j<pLen) repeated=false
        if (repeated) {
            i+=pLen-1
            repeated=false
        }
        else {
            res+=str.charAt(i)
        }
    }
    return res
}