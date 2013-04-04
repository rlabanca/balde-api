//
//  BaldeAPI.h
//  balde-api-ios
//
//  Created by Rodrigo Labanca on 4/2/13.
//  Copyright (c) 2013 org.baldeapi. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BaldeAPI : NSObject

+ (BaldeAPI*) getInBucket: (NSString*)bucket where: (id) filter sortBy: (id) sort skip: (int) skip limit: (int)limit;

+ (BaldeAPI*) getInBucket: (NSString*)bucket byId: (NSString*)id;

+ (BaldeAPI*) deleteInBucket: (NSString*)bucket byId: (NSString*)id;

+ (BaldeAPI*) postInBucket: (NSString*)bucket object: (id)object;

+ (BaldeAPI*) putInBucket: (NSString*)bucket withId: (NSString*)id object: (id)object;

- (void) executeOnHost: (NSString*) host withApiKey: (NSString*) apiKey andWhenFinishedPerform: (SEL) selector on: (id) obj;


@end
