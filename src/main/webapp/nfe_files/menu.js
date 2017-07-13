
$(document).ready(function() {

    if (navigator.userAgent.indexOf("MSIE 6") != -1) {
        $('#menu').css('height', '10px');
        $('#menu').css('width', '735px');
        $('#menu').css('top', '-18px');
        $('#barraDireita').css('left', '41px');
        $('#conteudo').css('position', 'relative');
        $('#conteudo').css('top', '-750px');
        $('#divContingencia').css('margin', '10px 10px 18px 20px');
    }

    $('#barra-governo').css('text-align', 'center');

    $('.divOculta').hide();
    $('.perguntaFaq a').click(
        function() {
            $(this).parent().next().slideToggle("slow");
        }
    );

    $('.botao').hover(
        function() {
            $(this).css('background-image', "url('imagens/meio_botao_on.png')");
        },
        function() {
            $(this).css('background-image', "url('imagens/meio_botao_off.png')");
        }
    );

    if (get_cookie("page_size") != null) {
        $('body').css('font-size', get_cookie("page_size") + 'px');
    }

    $('#imgZoomMenos').click(function() {
        var tamTexto = parseInt($('body').css('font-size').substring(0, 2)) - 1;
        $('body').css('font-size', tamTexto + 'px');
        //$('body *').css('font-size', tamTexto + 'px');
        set_cookie('page_size', tamTexto, 30);
    });

    $('#imgZoomMais').click(function() {
        var tamTexto = parseInt($('body').css('font-size').substring(0, 2)) + 1;
        $('body').css('font-size', tamTexto + 'px');
        //$('body *').css('font-size', tamTexto + 'px');
        set_cookie('page_size', tamTexto, 30);
    });


    $('.dropdown').each(
        function() {
            $(this).parent().eq(0).hover
            (
                function() {
                    $('.dropdown:eq(0)', this).show();
                },
                function() {
                    $('.dropdown:eq(0)', this).hide();
                }
            );
        }
    );
});


function exibirDetalhes(str) {

    document.getElementById(str).style.position = "relative";
    document.getElementById(str).style.top = "-50px"; //e.pageX;
    document.getElementById(str).style.left = "150px"; //e.pageY;
    setOpacity(0);
    document.getElementById(str).style.visibility = "visible";
    fadeInMyPopup();
}

function ocultarDetalhes(str) {
    document.getElementById(str).style.visibility= "hidden";
}

function trocarImagem(obj)
{
    nomeImg = obj.src;
    if( (indice = nomeImg.indexOf("Off")) > -1)
    {
        obj.src = nomeImg.replace( "Off", "On");
    } 
    else if( (indice = nomeImg.indexOf("On")) > -1)
    {
        obj.src = nomeImg.replace( "On", "Off");
    } 
}

function set_cookie(name,value,days) {
    if (days) {
        var date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        var expires = "; expires="+date.toGMTString();
    }
    else var expires = "";
    document.cookie = name+"="+value+expires+"; path=/";
}

function get_cookie(name) {
    var name_eq = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(name_eq) == 0) return c.substring(name_eq.length,c.length);
    }
    return null;
}

function setOpacity(value) {
    document.getElementById("styled_popup").style.opacity = value / 10;
    document.getElementById("styled_popup").style.filter = 'alpha(opacity=' + value * 10 + ')';
}

function setOpacitySuframa(value) {
    document.getElementById("internSuframa_popup").style.opacity = value / 10;
    document.getElementById("internSuframa_popup").style.filter = 'alpha(opacity=' + value * 10 + ')';
}

function fadeInMyPopup() {
    for (var i = 0; i <= 100; i++)
        setTimeout('setOpacity(' + (i / 10) + ')', 8 * i);
}

function fadeOutMyPopup() {
    for (var i = 0; i <= 100; i++) {
        setTimeout('setOpacity(' + (10 - i / 10) + ')', 8 * i);
    }
    setTimeout('closeMyPopup()', 800);
}

function closeMyPopup() {
    document.getElementById("styled_popup").style.display = "none"
}

function fireMyPopup() {
    setOpacity(0);
    document.getElementById("styled_popup").style.display = "block";
    fadeInMyPopup();
}




function fadeInInternSuframaPopup() {
    for (var i = 0; i <= 100; i++)
        setTimeout('setOpacitySuframa(' + (i / 10) + ')', 8 * i);
}

function fadeOutInternSuframaPopup() {
    for (var i = 0; i <= 100; i++) {
        setTimeout('setOpacitySuframa(' + (10 - i / 10) + ')', 8 * i);
    }
    setTimeout('closeInternSuframaPopup()', 800);
}

function fireInternSuframaPopup() {
    setOpacitySuframa(0);
    document.getElementById("internSuframa_popup").style.display = "block";
    fadeInInternSuframaPopup();
}

function closeInternSuframaPopup() {
    document.getElementById("internSuframa_popup").style.display = "none"
}




function atualizarChaveSelecionada(valor, id) {
    document.getElementById("ctl00_ContentPlaceHolder1_hdfChaveSelecionada").value = valor;
    document.getElementById("ctl00_ContentPlaceHolder1_hdfLinhaSelecionada").value = id;
}

function ocultarExibir() {
    var item = document.getElementById("configuracao");
    if (item.style.display == "inline") {
        item.style.display = "none";
    }
    else {
        item.style.display = "inline";
    }
}

function tooltip_findPosX(obj) {
    var curleft = 0;
    if (obj.offsetParent) {
        while (obj.offsetParent) {
            curleft += obj.offsetLeft
            obj = obj.offsetParent;
        }
    }
    else if (obj.x)
        curleft += obj.x;
    return curleft;
}

function tooltip_findPosY(obj) {
    var curtop = 0;
    if (obj.offsetParent) {
        while (obj.offsetParent) {
            curtop += obj.offsetTop
            obj = obj.offsetParent;
        }
    }
    else if (obj.y)
        curtop += obj.y;
    return curtop;
}

function tooltip_show(tooltipId, parentId, posX, posY) {
    it = document.getElementById(tooltipId);

    if ((it.style.top == '' || it.style.top == 0)
        && (it.style.left == '' || it.style.left == 0)) {
        // need to fixate default size (MSIE problem)
        it.style.width = it.offsetWidth + 'px';
        it.style.height = it.offsetHeight + 'px';

        img = document.getElementById(parentId);

        // if tooltip is too wide, shift left to be within parent 
        if (posX + it.offsetWidth > img.offsetWidth) posX = img.offsetWidth - it.offsetWidth;
        if (posX < 0) posX = 0;

        x = tooltip_findPosX(img) + posX;
        y = tooltip_findPosY(img) + posY;

        it.style.top = y + 'px';
        it.style.left = x + 'px';
    }

    it.style.visibility = 'visible';
}

function tooltip_hide(id) {
    it = document.getElementById(id);
    it.style.visibility = 'hidden';
}