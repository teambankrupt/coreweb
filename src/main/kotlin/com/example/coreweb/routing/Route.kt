package com.example.coreweb.routing

class Route {
    class V1 {
        companion object {
            private const val API = "/api"
            private const val VERSION = "/v1"
            private const val ADMIN = "/admin"

            // Address API's
            const val SEARCH_ADDRESSES = "$API$VERSION/addresses"
            const val CREATE_ADDRESSES = "$API$VERSION/addresses"
            const val FIND_ADDRESSES = "$API$VERSION/addresses/{id}"
            const val UPDATE_ADDRESSES = "$API$VERSION/addresses/{id}"
            const val DELETE_ADDRESSES = "$API$VERSION/addresses/{id}"

            // Division API's
            const val SEARCH_DIVISION = "$API$VERSION/divisions"
            const val CREATE_DIVISION = "$API$VERSION/divisions"
            const val FIND_DIVISION = "$API$VERSION/divisions/{id}"
            const val UPDATE_DIVISION = "$API$VERSION/divisions/{id}"
            const val DELETE_DIVISION = "$API$VERSION/divisions/{id}"

            // District API's
            const val SEARCH_DISTRICT = "$API$VERSION/districts"
            const val CREATE_DISTRICT = "$API$VERSION/districts"
            const val FIND_DISTRICT = "$API$VERSION/districts/{id}"
            const val UPDATE_DISTRICT = "$API$VERSION/districts/{id}"
            const val DELETE_DISTRICT = "$API$VERSION/districts/{id}"

            // Upazila API's
            const val SEARCH_UPAZILA = "$API$VERSION/upazilas"
            const val CREATE_UPAZILA = "$API$VERSION/upazilas"
            const val FIND_UPAZILA = "$API$VERSION/upazilas/{id}"
            const val UPDATE_UPAZILA = "$API$VERSION/upazilas/{id}"
            const val DELETE_UPAZILA = "$API$VERSION/upazilas/{id}"

            // Union API's
            const val SEARCH_UNION = "$API$VERSION/unions"
            const val CREATE_UNION = "$API$VERSION/unions"
            const val FIND_UNION = "$API$VERSION/unions/{id}"
            const val UPDATE_UNION = "$API$VERSION/unions/{id}"
            const val DELETE_UNION = "$API$VERSION/unions/{id}"

            // Village API's
            const val SEARCH_VILLAGES = "$API$VERSION/villages"
            const val CREATE_VILLAGES = "$API$VERSION/villages"
            const val FIND_VILLAGES = "$API$VERSION/villages/{id}"
            const val UPDATE_VILLAGES = "$API$VERSION/villages/{id}"
            const val DELETE_VILLAGES = "$API$VERSION/villages/{id}"

            /*
            Location API's
             */

            // LocationTypes (Admin)
            const val ADMIN_SEARCH_LOCATIONTYPES = "$ADMIN/location-types"
            const val ADMIN_CREATE_LOCATIONTYPE_PAGE = "$ADMIN/location-types/create"
            const val ADMIN_CREATE_LOCATIONTYPE = "$ADMIN/location-types"
            const val ADMIN_FIND_LOCATIONTYPE = "$ADMIN/location-types/{id}"
            const val ADMIN_UPDATE_LOCATIONTYPE_PAGE = "$ADMIN/location-types/{id}/update"
            const val ADMIN_UPDATE_LOCATIONTYPE = "$ADMIN/location-types/{id}"
            const val ADMIN_DELETE_LOCATIONTYPE = "$ADMIN/location-types/{id}/delete"

            // LocationTypes
            const val SEARCH_LOCATIONTYPES = "$API$VERSION/location-types"
            const val CREATE_LOCATIONTYPE = "$API$VERSION/location-types"
            const val FIND_LOCATIONTYPE = "$API$VERSION/location-types/{id}"
            const val UPDATE_LOCATIONTYPE = "$API$VERSION/location-types/{id}"
            const val DELETE_LOCATIONTYPE = "$API$VERSION/location-types/{id}"

            // Locations (Admin)
            const val ADMIN_SEARCH_LOCATIONS = "$ADMIN/locations"
            const val ADMIN_CREATE_LOCATION_PAGE = "$ADMIN/locations/create"
            const val ADMIN_CREATE_LOCATION = "$ADMIN/locations"
            const val ADMIN_FIND_LOCATION = "$ADMIN/locations/{id}"
            const val ADMIN_UPDATE_LOCATION_PAGE = "$ADMIN/locations/{id}/update"
            const val ADMIN_UPDATE_LOCATION = "$ADMIN/locations/{id}"
            const val ADMIN_DELETE_LOCATION = "$ADMIN/locations/{id}/delete"

            // Locations
            const val SEARCH_LOCATIONS = "$API$VERSION/location-types/{type_id}/locations"
            const val CREATE_LOCATION = "$API$VERSION/locations"
            const val FIND_LOCATION = "$API$VERSION/locations/{id}"
            const val UPDATE_LOCATION = "$API$VERSION/locations/{id}"
            const val DELETE_LOCATION = "$API$VERSION/locations/{id}"

            /*
            Contact API's
             */

            // Contacts
            const val SEARCH_CONTACTS = "$API$VERSION/contacts"
            const val CREATE_CONTACT = "$API$VERSION/contacts"
            const val FIND_CONTACT = "$API$VERSION/contacts/{id}"
            const val UPDATE_CONTACT = "$API$VERSION/contacts/{id}"
            const val DELETE_CONTACT = "$API$VERSION/contacts/{id}"

            // Contacts (Admin)
            const val ADMIN_SEARCH_CONTACTS = "$ADMIN/contacts"
            const val ADMIN_CREATE_CONTACT_PAGE = "$ADMIN/contacts/create"
            const val ADMIN_CREATE_CONTACT = "$ADMIN/contacts"
            const val ADMIN_FIND_CONTACT = "$ADMIN/contacts/{id}"
            const val ADMIN_UPDATE_CONTACT_PAGE = "$ADMIN/contacts/{id}/update"
            const val ADMIN_UPDATE_CONTACT = "$ADMIN/contacts/{id}"
            const val ADMIN_DELETE_CONTACT = "$ADMIN/contacts/{id}/delete"

        }
    }
}
