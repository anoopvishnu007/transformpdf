(function (global, factory) {
	typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@angular/core'), require('@angular/common')) :
	typeof define === 'function' && define.amd ? define(['exports', '@angular/core', '@angular/common'], factory) :
	(factory((global['ng2-adsense'] = {}),global.ng.core,global.ng.common));
}(this, (function (exports,core,common) { 'use strict';

var ADSENSE_TOKEN = new core.InjectionToken('AdsenseConfig');
var AdsenseComponent = /** @class */ (function () {
    function AdsenseComponent(config) {
        this.config = config;
        this.adRegion = 'page-' + Math.floor(Math.random() * 10000) + 1;
    }
    AdsenseComponent.prototype.ngOnInit = function () {
        var config = this.config;
        function use(source, defaultValue) {
            return config && source !== undefined ? source : defaultValue;
        }
        this.adClient = use(this.adClient, config.adClient);
        this.adSlot = use(this.adSlot, config.adSlot);
        this.adFormat = use(this.adFormat, config.adFormat || 'auto');
        this.display = use(this.display, config.display || 'block');
        this.width = use(this.width, config.width);
        this.height = use(this.height, config.height);
        this.layout = use(this.layout, config.layout);
        this.layoutKey = use(this.layoutKey, config.layoutKey);
        this.pageLevelAds = use(this.pageLevelAds, config.pageLevelAds);
        this.timeOutRetry = use(this.timeOutRetry, config.timeOutRetry || 200);
        this.adtest = use(this.adtest, config.adtest);
    };
    AdsenseComponent.prototype.ngAfterViewInit = function () {
        var _this = this;
        var res = this.push();
        if (res instanceof TypeError) {
            setTimeout(function () { return _this.push(); }, this.timeOutRetry);
        }
    };
    AdsenseComponent.prototype.push = function () {
        var p = {};
        if (this.pageLevelAds) {
            p.google_ad_client = this.adClient;
            p.enable_page_level_ads = true;
        }
        try {
            var adsbygoogle = window['adsbygoogle'];
            adsbygoogle.push(p);
            return true;
        }
        catch (e) {
            return e;
        }
    };
    return AdsenseComponent;
}());
AdsenseComponent.decorators = [
    { type: core.Component, args: [{
                selector: 'ng2-adsense,ng-adsense',
                template: "\n  <ins class=\"adsbygoogle\"\n    [ngStyle]=\"{'display': display, 'width.px': width, 'height.px': height }\"\n    [attr.data-ad-client]=\"adClient\"\n    [attr.data-ad-slot]=\"adSlot\"\n    [attr.data-ad-format]=\"adFormat\"\n    [attr.data-ad-region]=\"adRegion\"\n    [attr.data-layout]=\"layout\"\n    [attr.data-adtest]=\"adtest\"\n    [attr.data-layout-key]=\"layoutKey\">\n  </ins>\n  ",
                preserveWhitespaces: false,
                changeDetection: core.ChangeDetectionStrategy.OnPush,
            },] },
];
AdsenseComponent.ctorParameters = function () { return [
    { type: undefined, decorators: [{ type: core.Inject, args: [ADSENSE_TOKEN,] },] },
]; };
AdsenseComponent.propDecorators = {
    "adClient": [{ type: core.Input },],
    "adSlot": [{ type: core.Input },],
    "adFormat": [{ type: core.Input },],
    "adRegion": [{ type: core.Input },],
    "display": [{ type: core.Input },],
    "width": [{ type: core.Input },],
    "height": [{ type: core.Input },],
    "layout": [{ type: core.Input },],
    "layoutKey": [{ type: core.Input },],
    "pageLevelAds": [{ type: core.Input },],
    "timeOutRetry": [{ type: core.Input },],
    "adtest": [{ type: core.Input },],
};
var AdsenseModule = /** @class */ (function () {
    function AdsenseModule() {
    }
    AdsenseModule.forRoot = function (config) {
        if (config === void 0) { config = {}; }
        return {
            ngModule: AdsenseModule,
            providers: [{ provide: ADSENSE_TOKEN, useValue: config }],
        };
    };
    return AdsenseModule;
}());
AdsenseModule.decorators = [
    { type: core.NgModule, args: [{
                imports: [common.CommonModule],
                exports: [AdsenseComponent],
                declarations: [AdsenseComponent],
            },] },
];
AdsenseModule.ctorParameters = function () { return []; };

exports.AdsenseComponent = AdsenseComponent;
exports.AdsenseModule = AdsenseModule;
exports.ADSENSE_TOKEN = ADSENSE_TOKEN;

Object.defineProperty(exports, '__esModule', { value: true });

})));
//# sourceMappingURL=ng2-adsense.umd.js.map
