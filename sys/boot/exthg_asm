; Make any number of Executables to resident  V2.00   1988  Tony Tebby  QJUMP

; ex boot_exthing, list of files, output file

; The output file starts with a header which makes the following executables
; resident. The executable files are concantenated after a pair of long words
; which define the length and dataspace of each file

        section base

        xref    boot_concat

        include 'dev8_keys_err'
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_qdos_io'
        include 'dev8_keys_qdos_ioa'
        include 'dev8_keys_thg'

        data    $200            ; not much space

        bra.s   start
        dc.w    0,0,$4afb,14,'Exec to Thing '
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

        move.l  #$80000000+xth_end-xth_link,d6 ; dataspace req, extra at start
        jsr     boot_concat              ; concatenate
        bne.s   suicide

        ext.l   d6
        add.l   d6,d2                    ; add the header in

        lea     xth_end,a2               ; copy header starting here

head_copy
        move.w  -(a2),-(a1)
        subq.w  #2,d6
        bgt.s   head_copy

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


        section header

xth_link
        lea     xth_end,a5               ; the next file
        bra.s   xth_eloop

xth_loop
        move.l  (a5)+,d5                 ; data space
        lea     8(a5),a4                 ; the name
        moveq   #3,d4
        add.w   (a4),d4
        bclr    #0,d4                    ; name length rounded up


        moveq   #th_name+thh_strt+4,d1   ; allocate a bit of heap
        add.l   d4,d1
        moveq   #0,d2
        moveq   #sms.achp,d0
        trap    #do.sms2
        bne.s   xth_exit

        lea     th_name(a0),a2           ; copy name in
        move.w  d4,d0
xth_nmloop
        move.w  (a4)+,(a2)+
        subq.w  #2,d0
        bgt.s   xth_nmloop

        move.l  a2,th_thing(a0)          ; set thing address

        move.l  a5,d3
        sub.l   a2,d3                    ; offset to start rel base of thing

        move.l  #thh.flag,(a2)+          ; flag
        addq.l  #tht.exec,(a2)+          ; type
        move.l  d3,(a2)+                 ; offset to header
        addq.l  #8,d4                    ; BRA.L,0,$4afb
        move.l  d4,(a2)+                 ; length of header
        move.l  d5,(a2)+                 ; data space
        move.l  d3,(a2)+                 ; start address

        move.l  a0,a1
        moveq   #sms.lthg,d0
        trap    #do.sms2                 ; link in

        add.l   d6,a5                    ; jmp past this program
xth_eloop
        move.l  (a5)+,d6                 ; length of next
        bne.s   xth_loop

        moveq   #0,d0
xth_exit
        rts
xth_end
        end
