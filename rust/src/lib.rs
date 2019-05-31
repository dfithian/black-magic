#![cfg(target_os="android")]
#![allow(non_snake_case)]

extern crate reqwest;
extern crate select;

use std::io::{Error, ErrorKind};

use jni::JNIEnv;
use jni::objects::{JObject};
use jni::sys::{jstring};

use reqwest::{get};

use select::document::Document;
use select::predicate::{Class, Attr};

#[no_mangle]
pub unsafe extern fn Java_com_fithian_dan_blackmagic_MainActivity_sessionInfo(env: JNIEnv, _: JObject) -> jstring {
    let result = teamworks_acton("https://www.teamworksacton.com/currentleague.aspx?tIsAdult=1&strDefaultSport=Soccer".to_string());
    let msg = match result {
        Err(err) => format!("Failed to load: {}", err),
        Ok(x) => x.to_string(),
    };
    let output = env.new_string(msg).unwrap();
    output.into_inner()
}

fn get_html(url: String) -> Result<String, std::io::Error> {
    get(&url).map_err(|err| Error::new(ErrorKind::Other, err.to_string()))
        .and_then(|mut resp| match resp.status().is_success() {
            false => Err(Error::new(ErrorKind::Other, format!("Response was not successful: {} {}", resp.status(), resp.text().unwrap_or("<empty>".to_string())))),
            true => resp.text().map_err(|err| Error::new(ErrorKind::Other, err.to_string())),
        })
}

fn parse_html(html: String) -> Result<String, std::io::Error> {
    Document::from_read(html.as_bytes())
        .map(|doc| {
            doc.find(Class("leagues_info"))
                .flat_map(|league_info| {
                    league_info.find(Attr("role", "row"))
                })
                .map(|x| format!("{}", x.inner_html()))
                .collect()
        })
}

fn teamworks_acton(url: String) -> Result<String, String> {
    get_html(url).map_err(|err| err.to_string())
        .and_then(|html| parse_html(html).map_err(|err| err.to_string()))
}
