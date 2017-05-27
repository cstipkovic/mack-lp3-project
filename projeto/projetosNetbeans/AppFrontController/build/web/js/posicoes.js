var meuMapa;
var container;
var content;
var closer;
var infoAvatar = '';
//funcao executada quando a pagina html e carregada.
function init() {
    container = document.getElementById('popup');
    content = document.getElementById('popup-content');
    closer = document.getElementById('popup-closer');
    var overlay = new ol.Overlay(({
        element: container,
        autoPan: true,
        autoPanAnimation: {
            duration: 250
        }
    }));
    meuMapa = new ol.Map({
        target: 'MeuMapa',
        renderer: 'canvas',
        overlays: [overlay],
        view: new ol.View({
            projection: 'EPSG:900913',
            center: [-5193252.61684, -2698365.38923],
            zoom: 18
        })
    });
    var openStreetMapLayer = new ol.layer.Tile({
        source: new ol.source.OSM()
    });
    meuMapa.addLayer(openStreetMapLayer);
    closer.onclick = function () {
        overlay.setPosition(undefined);
        closer.blur();
        return false;
    };
//funcao assincrona executada quando um clique ocorre no mapa.
    meuMapa.on('singleclick', function (evt) {
//recuperamos as coordenadas do clique
        var coordinate = evt.coordinate;
//verificamos se o clique foi sobre uma feature
        var feature = meuMapa.forEachFeatureAtPixel(evt.pixel,
                function (feature, layer) {
                    return feature;
                });
        if (feature) { //busca informacao da feature
            buscaInfoAvatar(feature.get('login'), coordinate);
        } else { // loga posicao do usuario
            registraPosicao(coordinate);
            var hdms = ol.coordinate.toStringHDMS(ol.proj.transform(coordinate,
                    'EPSG:900913', 'EPSG:4326'));
            content.innerHTML = '<p>Posi&ccedil;&atilde;o Registrada para ' +
                    $('#loginUsuario').val() + ':</p><code>' + hdms + '</code>';
            overlay.setPosition(coordinate);
        }
    });
    function buscaInfoAvatar(login, coordinate) {
        console.log("Info Avatar:" + login);
        var urlString =
                'http://localhost:8080/AppFrontController/LP3Rest/lp3/infouseravatar/' + login;
        $.ajax({
            url: urlString,
            data: {format: 'json'
            },
            success: function (data) {
                console.log(data);
                content.innerHTML = data;
                overlay.setPosition(coordinate);
            },
            error: function (e) {
                console.log(e.message);
            },
            type: 'GET'
        });
    }
    function registraPosicao(coordinate) {
        var lonlat = ol.proj.transform(coordinate, 'EPSG:900913', 'EPSG:4326');
        var lon = lonlat[0];
        var lat = lonlat[1];
        var urlString = 'http://localhost:8080/AppFrontController/LP3Rest/lp3/novaposicao';
        var date = new Date();
        var isodate = date.toISOString();
        console.log(isodate);
        console.log("lat:" + lat);
        console.log("lon:" + lon);
        var login = $('#loginUsuario').val();
        console.log("login:" + login);
        $.ajax({
            url: urlString,
            contentType: "text/xml; charset=utf-8",
            data: "<posicao><login>" + login + "</login><timestamp>" + isodate +
                    "</timestamp><lat>" + lat + "</lat><lon>" + lon + "</lon></posicao>",
            dataType: "application/xml",
            success: function (data, status) {
                console.log(status);
                console.log(data);
            },
            error: function (e) {
                console.log(e.message);
            },
            type: 'PUT'
        });
    }
    $('#btnCarregaPosicoes').click(function () {
        busca($('#loginUsuario').val());
        return false;
    });
    function busca(login) {
        var xmlString;
        var urlString = 'http://localhost:8080/AppFrontController/LP3Rest/lp3/posicoes/';
        console.log(urlString);
        urlString = urlString.concat(login);
        console.log(urlString);
        $.ajax({
            url: urlString,
            data: {
                format: 'application/json'
            },
            dataType: 'json',
            success: function (data) {
                console.log(data);
                renderPoints(data);
            },
            error: function (e) {
                console.log(e.message);
            },
            type: 'GET'});
    }
    function renderPoints(data) {
        console.log("Rendering points");
        var len = data.length;
        var featureList = [];
        var iconStyle = new ol.style.Style({
            image: new ol.style.Icon(({
                anchor: [0.5, 0.5],
                anchorXUnits: 'fraction',
                anchorYUnits: 'pixels',
                opacity: 0.75,
                src: 'dados/r2d2.png'
            }))
        });
        for (i = 0; i < len; i++) {
            var point = new ol.geom.Point([parseFloat(data[i].lon), parseFloat(data[i].lat)]);
            point.transform('EPSG:4326', 'EPSG:900913');
            var feature = new ol.Feature({
                geometry: point,
                login: data[i].login,
                timestamp: data[i].timestamp
            });
            feature.setStyle(iconStyle);
            featureList.push(feature);
        }
        var vectorSource = new ol.source.Vector({
            features: featureList
        });
        var vectorLayer = new ol.layer.Vector({
            source: vectorSource
        });
        meuMapa.addLayer(vectorLayer);
        meuMapa.render();
    }
}