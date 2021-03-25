# Path definitions
set(pathToOpenCv D:/IMT-Solutions/OpenCVCamera/OpenCV-android-sdk)

cmake_minimum_required(VERSION 3.6.0)
set(LIBDIR ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs)

# Make sure we use flags for OpenGL in cpp
set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

# OpenCV includes
include_directories(${pathToOpenCv}/sdk/native/jni/include)

#Added by IDE on project create
set(CPP_ROOT ${CMAKE_CURRENT_SOURCE_DIR})
set(SRC_FILES ${CPP_ROOT}/common.hpp ${CPP_ROOT}/native-lib.cpp)
add_library( native-lib SHARED ${SRC_FILES} )

# OpenCV library
add_library( lib_opencv SHARED IMPORTED )
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${LIBDIR}/${ANDROID_ABI}/libopencv_java4.so)

# Link libraries
target_link_libraries( native-lib lib_opencv android log )