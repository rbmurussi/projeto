

function mascara(o,f){
v_obj=o
v_fun=f
setTimeout("execmascara()",1)
}

function execmascara(){
v_obj.value=v_fun(v_obj.value)
}


function soLetras(v){
return v.replace(/\d/g,"") //Remove tudo o que não é Letra
}

function soLetrasMA(v){
v=v.toUpperCase() //Maiúsculas
return v.replace(/\d/g,"") //Remove tudo o que não é Letra ->maiusculas
}

function soLetrasMI(v){
v=v.toLowerCase() //Minusculas
return v.replace(/\d/g,"") //Remove tudo o que não é Letra ->minusculas
}

function soNumeros(v){
return v.replace(/\D/g,"") //Remove tudo o que não é dígito
}


function soNumeros44(v){
v = v.replace(/\D/g,"") //Remove tudo o que não é Letra e trunca em 44 caracteres
return limitText(v, 44) //Remove tudo o que não é Letra e trunca em 44 caracteres
}

function limitText(text, limitNum) {
    if (text.length > limitNum) {
        text = text.substring(0, limitNum);
    }    
    return text
}
