; QL RAM disk Emulation   V2.01    1985  Tony Tebby   QJUMP

        section rd

        xdef    rd_emul         emulation check

        include 'dev8_keys_err'
;+++
; Checks for RAM disk emulation of other directory drive.
; If so, returns the address of the emulation routine in D0 and sets
; the emulated drive number / drive size (sectors) in D2
; If the RAM disk name is not recognised, it returns ERR.INAM.
;
; ******************************************************************
;
; This version is                        ***** NULL *****
;
; ******************************************************************
;
;       d0  r   error status or address of emulation routine
;       d1 c  p length of name
;       d2 cr   0 / preserved or set to drive nr/size
;       a1 c  p pointer to start of name
;
;       status return according to d0
;---
rd_emul
rde_inam
        moveq   #err.inam,d0
rde_exit
        rts
        end
