package xyz.tberghuis.floatingtimer.iap

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.flow.MutableSharedFlow
import xyz.tberghuis.floatingtimer.logd

class BillingClientWrapper(
  context: Context
) : PurchasesUpdatedListener {

  // future.txt refactor billingClient as class dependency
  private val billingClient = BillingClient.newBuilder(context)
    .setListener(this)
    .enablePendingPurchases()
    .build()

  // doitwrong: this is wack but it works
  private var purchasesUpdatedContinuation: Continuation<BillingResult>? = null

  override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
    logd("purchasesUpdatedListener")

    // todo await after acknowledgePurchase to resume continuation

    try {
      purchasesUpdatedContinuation?.resume(billingResult)
    } catch (e: RuntimeException) {
      // this happens sometimes for some reason ???
      Log.e("BillingClientWrapper", "error: $e")
    }

    if (billingResult.responseCode != BillingClient.BillingResponseCode.OK || purchases.isNullOrEmpty()) {
      return
    }
    for (purchase in purchases) {
      acknowledgePurchase(purchase)
    }
  }

  private fun acknowledgePurchase(purchase: Purchase) {
    if (!purchase.isAcknowledged) {
      val params = AcknowledgePurchaseParams.newBuilder()
        .setPurchaseToken(purchase.purchaseToken)
        .build()
      billingClient.acknowledgePurchase(
        params
      ) { billingResult ->
        logd("acknowledgePurchase billingResult $billingResult")
      }
    }
  }

  suspend fun startBillingConnection(): BillingResult = suspendCoroutine { continuation ->
    val billingClientStateListener = object : BillingClientStateListener {
      override fun onBillingServiceDisconnected() {
        // probably best to let the app crash if this called???
        logd("onBillingServiceDisconnected")
      }

      override fun onBillingSetupFinished(billingResult: BillingResult) {
        continuation.resume(billingResult)
      }
    }
    billingClient.startConnection(billingClientStateListener)
  }

  // rewrite with my own result type, or arrow either
  // allow me to show snackbars on error cases

  // startBillingConnection must be called first
  suspend fun checkHaloColourPurchased(
  ): Boolean = suspendCoroutine { continuation ->
    val queryPurchasesParams =
      QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build()

    val purchasesResponseListener =
      PurchasesResponseListener { billingResult: BillingResult, purchases: MutableList<Purchase> ->
        logd("purchasesResponseListener")
        logd("billingResult $billingResult")
        logd("purchases $purchases")
        var purchased = false
        when (billingResult.responseCode) {
          BillingClient.BillingResponseCode.OK -> {
            for (purchase in purchases) {
              if (purchase.products.contains("halo_colour")) {
                purchased = true
                break
              }
            }
          }

          else -> {
            // TODO snackbar debugMessage
          }
        }
        continuation.resume(purchased)
      }
    billingClient.queryPurchasesAsync(queryPurchasesParams, purchasesResponseListener)
  }


  fun endBillingConnection() {
    logd("Terminating connection")
    billingClient.endConnection()
  }

  // todo return PurchaseHaloColourChangeResult
  // future.txt use arrow.kt Either
  suspend fun purchaseHaloColourChange(activity: Activity): BillingResult? {
    // do i need withContext(IO) ??? assume no

    // todo show error snackbar
    val productDetails = getHaloColourProductDetails() ?: return null

    return suspendCoroutine { continuation ->
      // this is a hack, shouldn't be a problem if using fresh BillingClientWrapper
      // for each call to purchaseHaloColourChange()
      purchasesUpdatedContinuation = continuation

      val productDetailsParams =
        BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails)
          .build()

      val params = BillingFlowParams.newBuilder()
        .setProductDetailsParamsList(listOf(productDetailsParams))
        .build()
      billingClient.launchBillingFlow(activity, params)
    }
  }


  suspend fun getHaloColourProductDetails(): ProductDetails? =
    suspendCoroutine { continuation ->
      val productId = "halo_colour"
      val product = QueryProductDetailsParams.Product.newBuilder()
        .setProductId(productId)
        .setProductType(BillingClient.ProductType.INAPP)
        .build()

      val params = QueryProductDetailsParams.newBuilder().setProductList(listOf(product)).build()

      val productDetailsResponseListener =
        ProductDetailsResponseListener { billingResult, productDetailsList ->
          logd("productDetailsResponseListener")
          logd("billingResult $billingResult")
          logd("productDetailsList $productDetailsList")

          val productDetails = productDetailsList.find {
            it.productId == "halo_colour"
          }
          continuation.resume(productDetails)
        }
      billingClient.queryProductDetailsAsync(params, productDetailsResponseListener)
    }


  companion object {
    // use this function to start connection, run callback, end connection
    // poor design, future.txt use arrow.kt to deal with error handling
    suspend fun run(
      context: Context,
      errorMessageFlow: MutableSharedFlow<String>? = null,
      callback: suspend (BillingClientWrapper) -> Unit
    ) {
      val client = BillingClientWrapper(context)
      val result = client.startBillingConnection()
      if (result.responseCode == BillingClient.BillingResponseCode.OK) {
        callback(client)
      } else {
        // i could move this into startBillingConnection
        errorMessageFlow?.emit(result.debugMessage)
      }
      client.endBillingConnection()
    }
  }
}