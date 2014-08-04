################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/impl/ANN.cpp \
../src/impl/ANNInformation.cpp \
../src/impl/Prediction.cpp 

OBJS += \
./src/impl/ANN.o \
./src/impl/ANNInformation.o \
./src/impl/Prediction.o 

CPP_DEPS += \
./src/impl/ANN.d \
./src/impl/ANNInformation.d \
./src/impl/Prediction.d 


# Each subdirectory must supply rules for building sources it contributes
src/impl/%.o: ../src/impl/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I/usr/local/include/opencv -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o"$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


