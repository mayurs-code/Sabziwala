package com.example.sabziwala.Service.communicator;


import com.example.sabziwala.Service.request.GetInventoryRequest;
import com.example.sabziwala.Service.request.GetOrdersRequest;
import com.example.sabziwala.Service.request.GetProfileRequest;
import com.example.sabziwala.Service.request.OrderDetailRequest;
import com.example.sabziwala.Service.request.UpdateCoordinatesRequest;
import com.example.sabziwala.Service.request.UpdateInventoryRequest;
import com.example.sabziwala.Service.request.UpdateOrderStatusRequest;
import com.example.sabziwala.Service.request.UserLoginRequest;
import com.example.sabziwala.Service.response.ChangeActiveStatusResponse;
import com.example.sabziwala.Service.response.GetInventoryResponse;
import com.example.sabziwala.Service.response.GetOrdersResponse;
import com.example.sabziwala.Service.response.GetProfileResponse;
import com.example.sabziwala.Service.response.NotificationResponse;
import com.example.sabziwala.Service.response.OrderDetailResponse;
import com.example.sabziwala.Service.response.UpdateCoordinatesResponse;
import com.example.sabziwala.Service.response.UpdateInventoryResponse;
import com.example.sabziwala.Service.response.UpdateOrderStatusResponse;
import com.example.sabziwala.Service.response.UserLoginResponse;

public class ServerCommunicator {

    public static void userLogin(Connector connector, UserLoginRequest request) {
        connector.callServer("/login", Connector.METHOD_POST,
                Utils.getGson().toJson(request), "", UserLoginResponse.class);
    }

    public static void getOrdersList(Connector connector, GetOrdersRequest request, String key) {
        connector.callServer("/get_order_list", Connector.METHOD_POST,
                Utils.getGson().toJson(request), key, GetOrdersResponse.class);
    }

    public static void getProfile(Connector connector, GetProfileRequest request, String key) {
        connector.callServer("/get_profile", Connector.METHOD_POST,
                Utils.getGson().toJson(request), key, GetProfileResponse.class);
    }
    public static void changeStatus(Connector connector, String key) {
        connector.callServer("/toggle_active_status", Connector.METHOD_POST,
                Utils.getGson().toJson(null), key, ChangeActiveStatusResponse.class);
    }
    public static void updateCoordinates(Connector connector , UpdateCoordinatesRequest request, String key) {
        connector.callServer("/update_coordinate", Connector.METHOD_POST,
                Utils.getGson().toJson(request), key, UpdateCoordinatesResponse.class);
    }public static void updateOrderStatus(Connector connector , UpdateOrderStatusRequest request, String key) {
        connector.callServer("/change_order_status", Connector.METHOD_POST,
                Utils.getGson().toJson(request), key, UpdateOrderStatusResponse.class);
    }
    public static void giveOrderDetails(Connector connector, OrderDetailRequest request, String key) {
        connector.callServer("/get_order_detail", Connector.METHOD_POST,
                Utils.getGson().toJson(request), key, OrderDetailResponse.class);
    }
    public static void getInventory(Connector connector , GetInventoryRequest request, String key) {
        connector.callServer("/get_inventory_list", Connector.METHOD_POST,
                Utils.getGson().toJson(request), key, GetInventoryResponse.class);
    }public static void getAllInventory(Connector connector , GetInventoryRequest request, String key) {
        connector.callServer("/get_all_inventory_products", Connector.METHOD_POST,
                Utils.getGson().toJson(request), key, GetInventoryResponse.class);
    }public static void getNotifications(Connector connector  , String key) {
        connector.callServer("/get_notifications", Connector.METHOD_POST,
                Utils.getGson().toJson(null), key, NotificationResponse.class);
    }public static void updateInventory(Connector connector  , UpdateInventoryRequest request, String key) {
        connector.callServer("/update_invetory", Connector.METHOD_POST,
                Utils.getGson().toJson(request), key, UpdateInventoryResponse.class);
    }

}
