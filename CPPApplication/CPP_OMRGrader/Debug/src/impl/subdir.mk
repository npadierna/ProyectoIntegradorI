################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/impl/ExamProcess.cpp \
../src/impl/ImageProcess.cpp 

OBJS += \
./src/impl/ExamProcess.o \
./src/impl/ImageProcess.o 

CPP_DEPS += \
./src/impl/ExamProcess.d \
./src/impl/ImageProcess.d 


# Each subdirectory must supply rules for building sources it contributes
src/impl/%.o: ../src/impl/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I/usr/local/include/opencv -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o"$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


