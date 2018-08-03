<?php

namespace App\Http\Controllers;


use Illuminate\Http\Request;

use App\Http\Requests;

use Ivory\GoogleMap\Map;

use Ivory\GoogleMap\MapTypeId;

use Ivory\GoogleMap\Helper\MapHelper;

use Ivory\GoogleMap\Overlays\Animation;

use Ivory\GoogleMap\Overlays\Marker;

class MapController extends Controller
{
    public function buildMap(){
    	$map = new Map();
    	$mapHelper = new MapHelper();

    	$marker = new Marker();

    	$marker->setPrefixJavascriptVariable('marker_');
		$marker->setPosition(18.929312,72.8300335, true);
		$marker->setAnimation(Animation::DROP);

		$marker->setOptions(array(
		    'clickable' => false,
		    'flat'      => true,
		));

    	$map->setPrefixJavascriptVariable('map_');
		$map->setHtmlContainerId('map_canvas');

		$map->setAsync(false);
		$map->setAutoZoom(true);

		$map->addMarker($marker);

		//$map->setBound(-4.2, -7.8, 5.2, 2.8, true, true);

		$map->setMapOption('mapTypeId', 'roadmap');

		$map->setMapOptions(array(
		    'disableDefaultUI'       => false,
		    'disableDoubleClickZoom' => true,
		));

		$map->setStylesheetOptions(array(
		    'width'  => '100%',
		    'height' => '100%',
		));

		//return view('maps')->with('map',$map)->with('mapHelper',$mapHelper);
    	return $mapHelper->render($map);
    }
}
