package com.bethel.mycoolweather.json;

import java.util.List;

/**
 * @author: Bethel
 * @date: 2020-10-15 14:50
 * @desc:
 */
public class Refer {

        private List<String> sources;
        private List<String> license;
        public void setSources(List<String> sources) {
            this.sources = sources;
        }
        public List<String> getSources() {
            return sources;
        }

        public void setLicense(List<String> license) {
            this.license = license;
        }
        public List<String> getLicense() {
            return license;
        }

}
