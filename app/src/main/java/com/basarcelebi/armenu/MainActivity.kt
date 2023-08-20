package com.basarcelebi.armenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.basarcelebi.armenu.ui.theme.ARMenuTheme
import com.basarcelebi.armenu.ui.theme.Translucent
import com.google.ar.core.Config
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ARMenuTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize())
                    {
                        Menu(modifier = Modifier.align(Alignment.BottomCenter))
                    }
                }
            }
        }
    }
}


@Composable
fun Menu(modifier: Modifier)
{
    var currentIndex by remember {
        mutableStateOf(0)
    }
    val itemsList = listOf(
        Food("burger", R.drawable.burger),
        Food("instant", R.drawable.instant),
        Food("momos", R.drawable.momos),
        Food("pizza", R.drawable.pizza),
        Food("ramen", R.drawable.ramen)
    )
    fun updateIndex(offset:Int)
    {
        currentIndex = (itemsList.size + currentIndex + offset) % itemsList.size
    }
    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround) {
        
        IconButton(onClick = {
            updateIndex(-1)
        }) {
            Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24), contentDescription = "previous")
            
        }
        CircularImage(imageId = itemsList[currentIndex].imageId)
        IconButton(onClick = {
            updateIndex(1)
        }) {
            Icon(painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24), contentDescription = "next")

        }

    }


}
@Composable
fun CircularImage(
    modifier: Modifier=Modifier,
    imageId: Int)
{
    Box(modifier = modifier
        .size(140.dp)
        .clip(CircleShape)
        .border(width = 3.dp, Translucent)){
        Image(painter = painterResource(id = imageId), contentDescription = null, Modifier.size(140.dp), contentScale = ContentScale.FillBounds)

    }
}

@Composable
fun ArScreen() {
    val nodes = remember {
        mutableListOf<ArNode>()
    }
    val modelNode = remember {
        mutableStateOf<ArModelNode?>(null)
    }
    val placeModelButton = remember {
        mutableStateOf(false)
    }
    ARScene(modifier = Modifier.fillMaxSize(),
        nodes = nodes,
        planeRenderer = true,
        onCreate = {arSceneView -> arSceneView.lightEstimationMode = Config.LightEstimationMode.DISABLED
        arSceneView.planeRenderer.isShadowReceiver = false
        modelNode.value= ArModelNode(arSceneView.engine,PlacementMode.INSTANT).apply {
            loadModelGlbAsync(
                glbFileLocation = "",

            ){

            }
            onAnchorChanged = {
                
            }
            onHitResult = {node, hitResult ->  }
            nodes.add(modelNode.value!!)
        }
        }
        )
    
}

data class Food(var name:String, var imageId:Int)