; ATARI:  create compressed bootloader type file

; ex boot_compress, list of files, output file

; The compressed output file starts with the first input file, subsequent files
; are concantenated after a long word which defines the length of each file

        section base

        xref    boot_concat
        xref    boot_setlen
        xref    cmp_cpfil

        include 'dev8_keys_err'
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_qdos_io'
        include 'dev8_keys_qdos_ioa'

        data    $200            ; not much space

        bra.s   start
        dc.w    0,0,$4afb,14,'Boot Compress '
        dc.w    $4afb           ; Special job

        section special

        section main
start
 trap #15
        add.l   a4,a6                    ; base of data area

        moveq   #err.ipar,d0
        move.w  (sp)+,d5                 ; number of channels
        subq.w  #1,d5                    ; two?
        ble.s   suicide                  ; ... no
        lea     (sp),a5                  ; pointer to file list

        jsr     boot_concat              ; concatenate
        bne.s   suicide

        jsr     boot_setlen              ; and set length

        jsr     cmp_cpfil                ; compress file
        bne.s   suicide

        move.l  a0,a1
        move.l  d1,d2                    ; set compressed file pointers
        move.l  (a5),a0                  ; output channel ID
        moveq   #iof.save,d0
        bsr.s   do_io

suicide
        move.l  d0,d3                    ; error return
        moveq   #sms.frjb,d0             ; force remove
        moveq   #myself,d1               ; ... me
        trap    #do.sms2

do_io
        moveq   #forever,d3
        trap    #do.io
        tst.l   d0
        bne.s   suicide
rts
        rts

        end
