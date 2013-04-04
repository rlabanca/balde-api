//
//  BaldeAPIResponse.h
//  balde-api-ios
//
//  Created by Rodrigo Labanca on 4/2/13.
//  Copyright (c) 2013 org.baldeapi. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BaldeAPIResponse : NSObject

@property (nonatomic,strong) NSDictionary *response;
@property (nonatomic) BOOL array;
@property (nonatomic) int statusCode;

- (BOOL) success;

@end
