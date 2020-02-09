; Uninitialised data definitions
; 2006.10.01	1.00	created by BC

_bss    SEGMENT DWORD PUBLIC 'BSS'

        ALIGN 4

qxl_sbuffer     BYTE 16384 DUP (?)

qxl_sbufftop WORD ?

        ALIGN 4

qxl_rbuffer     BYTE 16384 DUP (?)

qxl_buffsize = 1024
qxl_buffroom = qxl_buffsize - 1
qxl_buffmin = 512 ; (min space for flow on)
qxl_buffrts = 64 ; min space before RTS off
qxl_buffalloc = qxl_buffsize + 8

qxl_lptbuff BYTE qxl_buffalloc*4 DUP (?) ; for the moment, max 4 output ports
qxl_combuff BYTE qxl_buffalloc*4*2 DUP (?) ; for the moment, max 4 IO ports

qxl_datatop WORD ?

_bss    ENDS
