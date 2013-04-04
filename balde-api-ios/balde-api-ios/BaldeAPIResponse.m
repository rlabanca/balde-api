//
//  BaldeAPIResponse.m
//  balde-api-ios
//
//  Created by Rodrigo Labanca on 4/2/13.
//  Copyright (c) 2013 org.baldeapi. All rights reserved.
//

#import "BaldeAPIResponse.h"

@implementation BaldeAPIResponse

@synthesize response = _response;
@synthesize array = _array;
@synthesize statusCode = _statusCode;

- (BOOL) success {
    return self.statusCode / 100 == 2;
}

@end
