//
//  BaldeAPI.m
//  balde-api-ios
//
//  Created by Rodrigo Labanca on 4/2/13.
//  Copyright (c) 2013 org.baldeapi. All rights reserved.
//

#import "BaldeAPI.h"
#import "BaldeAPIResponse.h"

@interface BaldeAPI()

@property (nonatomic,strong) NSString *method;
@property (nonatomic,strong) NSString *host;
@property (nonatomic,strong) NSString *bucket;
@property (nonatomic,strong) NSString *apiKey;
@property (nonatomic,strong) NSString *id;
@property (nonatomic,strong) NSDictionary *filter;
@property (nonatomic,strong) NSDictionary *sort;
@property (nonatomic) int skip;
@property (nonatomic) int limit;
@property (nonatomic,strong) id object;
@property (nonatomic,strong) id callbackObject;
@property (nonatomic) SEL callbackSelector;

@end

@implementation BaldeAPI

@synthesize method = _method;
@synthesize host = _host;
@synthesize bucket = _bucket;
@synthesize apiKey = _apiKey;
@synthesize id = _id;
@synthesize filter = _filter;
@synthesize skip = _skip;
@synthesize limit = _limit;
@synthesize object = _object;
@synthesize callbackObject = _callbackObject;
@synthesize callbackSelector = _callbackSelector;

+ (BaldeAPI*) getInBucket: (NSString*)bucket where: (NSDictionary*) filter sortBy: (NSDictionary*) sort skip: (int) skip limit: (int)limit {
    BaldeAPI *api = [[BaldeAPI alloc]init];
    api.method = @"GET";
    api.bucket = bucket;
    api.filter = filter;
    api.sort = sort;
    api.skip = skip;
    api.limit = limit;
    return api;
}

+ (BaldeAPI*) getInBucket: (NSString*)bucket byId: (NSString*)id {
    BaldeAPI *api = [[BaldeAPI alloc]init];
    api.method = @"GET";
    api.bucket = bucket;
    api.id = id;
    return api;
}

+ (BaldeAPI*) deleteInBucket: (NSString*)bucket byId: (NSString*)id {
    BaldeAPI *api = [[BaldeAPI alloc]init];
    api.method = @"DELETE";
    api.bucket = bucket;
    api.id = id;
    return api;
}

+ (BaldeAPI*) postInBucket: (NSString*)bucket object: (id)object {
    BaldeAPI *api = [[BaldeAPI alloc]init];
    api.method = @"POST";
    api.bucket = bucket;
    api.object = object;
    return api;
}

+ (BaldeAPI*) putInBucket: (NSString*)bucket withId: (NSString*)id object: (id)object {
    BaldeAPI *api = [[BaldeAPI alloc]init];
    api.method = @"PUT";
    api.bucket = bucket;
    api.id = id;
    api.object = object;
    return api;
}

- (void) executeOnHost: (NSString*) host withApiKey: (NSString*) apiKey andWhenFinishedPerform: (SEL) selector on: (id) obj {
    self.host = host;
    self.apiKey = apiKey;
    self.callbackObject = obj;
    self.callbackSelector = selector;
    [self performSelectorInBackground:@selector(execute) withObject:nil];
}

- (void) execute {
    
    NSString *url = [self buildUrl];
    
    NSLog(@"URL: %@", url);
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:
                                    [NSURL URLWithString:url]];
    [request setHTTPMethod: self.method];
    [request setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [request setValue:self.apiKey forHTTPHeaderField:@"X-API-Key"];
    
    if (self.object) {
        NSData *json = [NSJSONSerialization dataWithJSONObject:self.object options:0 error:nil];
        [request setHTTPBody:json];
    }
    
    NSError *error = [[NSError alloc] init];
    
    NSHTTPURLResponse *responseCode = nil;
    
    NSData *responseData = [NSURLConnection sendSynchronousRequest:request             returningResponse:&responseCode error:&error];
    
    BaldeAPIResponse *response = [[BaldeAPIResponse alloc]init];
    response.statusCode = responseCode.statusCode;
    
    NSLog(@"status code: %d", response.statusCode);
    
    if ([response success]) {
        
        NSError *error;
        NSDictionary *json = [NSJSONSerialization
                              JSONObjectWithData:responseData
                              options:kNilOptions 
                              error:&error];
        
        response.response = json;
        
    } else {
        NSLog(@"%@", error);
    }
    
    [self.callbackObject performSelectorOnMainThread:self.callbackSelector withObject:response waitUntilDone:NO];
}

- (NSString*) buildUrl {
    NSMutableString *url = [[NSMutableString alloc]init];
    
    [url appendString:self.host];
    [url appendString:@"/"];
    [url appendString:self.bucket];
    
    if (self.id) {
        [url appendString:@"/"];
        [url appendString:self.id];
    }
    
    if (self.filter || self.sort || self.skip || self.limit) {
        
        [url appendString:@"?"];
        
        if (self.filter) {
            [url appendFormat:@"filter=%@&", [self serializeJson:self.filter]];
        }
        
        if (self.sort) {
            [url appendFormat:@"sort=%@&", [self serializeJson:self.sort]];
        }
        
        if (self.skip) {
            [url appendFormat:@"skip=%d&", self.skip];
        }
        
        if (self.limit) {
            [url appendFormat:@"limit=%d&", self.limit];
        }
        
    }
    
    return url;
    
}

- (NSString*) serializeJson: (NSDictionary *)json {
    NSError *error = [[NSError alloc] init];
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:json options:0 error:&error];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData
                          encoding:NSUTF8StringEncoding];
    jsonString = [self encodeURL:jsonString];
    NSLog(@"json string: %@", jsonString);
    return jsonString;
}

- (NSString*)encodeURL:(NSString *)string
{
    NSString *newString = (__bridge NSString *)CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault, (__bridge CFStringRef)string, NULL, CFSTR(":/?#[]@!$ &'()*+,;=\"<>%{}|\\^~`"), CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding));
    
    if (newString)
    {
        return newString;
    }
    
    return @"";
}

@end
