[#import "htmloutput.ftl" as htmloutput]
[#import "docbook-html.ftl" as default]
[#import "customizations.ftl" as customizations]
[@htmloutput.Html .node.title]
[#recurse  using [customizations, htmloutput, default]]
[/@]