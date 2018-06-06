package ikakus.com.tbilisinav.modules.nearbystops

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.data.database.models.MapBusStop
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteStop
import ikakus.com.tbilisinav.utils.GMapHelper
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.route_map_view_layout.view.*


