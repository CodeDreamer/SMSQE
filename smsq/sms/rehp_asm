; Return to heap  V2.01    1986  Tony Tebby  QJUMP

        section sms

        xdef    sms_rehp

        xref    mem_rehp                 ; return to heap
        xref    sms_rte                  ; return

        include 'dev8_keys_psf'
;+++
;       d0      0
;       d1 c  p length to return
;       a0 c s  base address of area to return
;       a1 c  p pointer to free space link
;       a6      base address for a0
;
;       all other registers preserved
;---
sms_rehp
        move.l  psf_a6(a7),d7            ; base address
        add.l   d7,a0                    ; ... make absolute
        add.l   d7,a1
        bsr.l   mem_rehp                 ; return
        sub.l   d7,a0                    ; ... make relative
        sub.l   d7,a1
        bra.l   sms_rte
        end
