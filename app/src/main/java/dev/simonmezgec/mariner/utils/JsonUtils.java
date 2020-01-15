package dev.simonmezgec.mariner.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dev.simonmezgec.mariner.Constant;
import dev.simonmezgec.mariner.database.DataAsset;

/** Class containing JSON parsing utilities. */
public class JsonUtils {

    /** Parses the input JSON string and returns a list of DataAsset objects with parsed values. */
    public static List<DataAsset> parseDataAssetsJson(String json) {
        List<DataAsset> allDataAssets = new ArrayList<>();

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONArray resultsJson = jsonObj.getJSONArray(Constant.JSON_RESULTS);

            for (int i = 0; i < resultsJson.length(); i++) {
                JSONObject dataAssetObj = resultsJson.getJSONObject(i);
                JSONArray publicKey = dataAssetObj.getJSONArray(Constant.JSON_PUBLIC_KEY);
                JSONObject publicKeyObj = publicKey.getJSONObject(0);
                String oceanId = publicKeyObj.getString(Constant.JSON_ID);
                String owner = publicKeyObj.getString(Constant.JSON_OWNER);
                JSONArray service = dataAssetObj.getJSONArray(Constant.JSON_SERVICE);
                JSONObject serviceObj = service.getJSONObject(0);
                JSONObject attributes = serviceObj.getJSONObject(Constant.JSON_ATTRIBUTES);
                JSONObject main = attributes.getJSONObject(Constant.JSON_MAIN);
                String type = main.getString(Constant.JSON_TYPE);
                String name = main.getString(Constant.JSON_NAME);
                String author = main.getString(Constant.JSON_AUTHOR);
                String license = main.getString(Constant.JSON_LICENSE);
                double price = main.getDouble(Constant.JSON_PRICE) / Math.pow(10, 18);

                String dateCreatedString = main.getString(Constant.JSON_DATE_CREATED);
                String datePublishedString = main.getString(Constant.JSON_DATE_PUBLISHED);
                Date dateCreated = null;
                Date datePublished = null;
                SimpleDateFormat dateFormat
                        = new SimpleDateFormat(Constant.JSON_DATE_FORMAT, Locale.ENGLISH);
                try {
                    dateCreated = dateFormat.parse(dateCreatedString);
                    datePublished = dateFormat.parse(datePublishedString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                StringBuilder fileContentTypes = new StringBuilder();
                StringBuilder fileSizes = new StringBuilder();
                JSONArray files = main.getJSONArray(Constant.JSON_FILES);
                for (int j = 0; j < files.length(); j++) {
                    JSONObject fileObj = files.getJSONObject(j);
                    if (!fileObj.has(Constant.JSON_CONTENT_TYPE)
                            || fileObj.getString(Constant.JSON_CONTENT_TYPE).equals("")) {
                        if (fileContentTypes.toString().equals(""))
                            fileContentTypes = new StringBuilder(Constant.JSON_UNKNOWN_VALUE);
                        else fileContentTypes.append(",").append(Constant.JSON_UNKNOWN_VALUE);
                    }
                    else {
                        if (fileContentTypes.toString().equals("")) {
                            fileContentTypes = new StringBuilder(
                                    fileObj.getString(Constant.JSON_CONTENT_TYPE));
                        }
                        else {
                            fileContentTypes.append(",").append(
                                    fileObj.getString(Constant.JSON_CONTENT_TYPE));
                        }
                    }
                    if (!fileObj.has(Constant.JSON_CONTENT_LENGTH)
                            || fileObj.getString(Constant.JSON_CONTENT_LENGTH).equals("")) {
                        if (fileSizes.toString().equals(""))
                            fileSizes = new StringBuilder(Constant.JSON_UNKNOWN_NUMBER);
                        else fileSizes.append(",").append(Constant.JSON_UNKNOWN_NUMBER);

                    }
                    else {
                        if (fileSizes.toString().equals("")) {
                            fileSizes = new StringBuilder(
                                    fileObj.getString(Constant.JSON_CONTENT_LENGTH));
                        }
                        else {
                            fileSizes.append(",").append(
                                    fileObj.getString(Constant.JSON_CONTENT_LENGTH));
                        }
                    }
                }

                JSONObject additionalInformation
                        = attributes.getJSONObject(Constant.JSON_ADDITIONAL_INFORMATION);
                StringBuilder tags = new StringBuilder();
                if (additionalInformation.has(Constant.JSON_TAGS)) {
                    JSONArray tagsArray = additionalInformation.getJSONArray(Constant.JSON_TAGS);
                    for (int j = 0; j < tagsArray.length(); j++) {
                        if (tags.toString().equals(""))
                            tags = new StringBuilder(tagsArray.getString(j));
                        else tags.append(",").append(tagsArray.getString(j));
                    }
                }

                StringBuilder categories = new StringBuilder();
                if (additionalInformation.has(Constant.JSON_CATEGORIES)) {
                    JSONArray categoriesArray
                            = additionalInformation.getJSONArray(Constant.JSON_CATEGORIES);
                    for (int j = 0; j < categoriesArray.length(); j++) {
                        if (categories.toString().equals(""))
                            categories = new StringBuilder(categoriesArray.getString(j));
                        else categories.append(",").append(categoriesArray.getString(j));
                    }
                }

                String description = additionalInformation.getString(Constant.JSON_DESCRIPTION);
                // Hardcode the URL for the Commons Marketplace. Will differ for each marketplace in
                // future versions.
                String link = Constant.JSON_COMMONS_LINK + oceanId;

                DataAsset dataAsset = new DataAsset();
                dataAsset.setOceanId(oceanId);
                dataAsset.setOwner(owner);
                dataAsset.setType(type);
                dataAsset.setName(name);
                dataAsset.setDateCreated(dateCreated);
                dataAsset.setDatePublished(datePublished);
                dataAsset.setAuthor(author);
                dataAsset.setLicense(license);
                dataAsset.setPrice(price);
                dataAsset.setFileContentTypes(fileContentTypes.toString());
                dataAsset.setFileSizes(fileSizes.toString());
                dataAsset.setTags(tags.toString());
                dataAsset.setCategories(categories.toString());
                dataAsset.setDescription(description);
                dataAsset.setLink(link);
                allDataAssets.add(dataAsset);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return allDataAssets;
    }
}