; Allocate in heap  V2.01    1986  Tony Tebby  QJUMP

        section sms

        xdef    sms_alhp
        xref    mem_alhp                 ; allocate in heap
        xref    sms_rte                  ; return

        include 'dev8_keys_psf'
;+++
;       d0      0 or out of memory
;       d1 cr   space required / space allocated
;       a0 cr   ptr to free space link / base address of area allocated
;       a6      (trap entry) base address for a0
;
;       all other registers preserved
;---
sms_alhp
        move.l  psf_a6(a7),d7            ; base address
        add.l   d7,a0                    ; ... make absolute
        bsr.l   mem_alhp                 ; allocate
        sub.l   d7,a0                    ; ... make relative
        bra.l   sms_rte
        end
