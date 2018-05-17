; NET         V2.00      1990  Tony Tebby   QJUMP
;
;       NET nr
;
        section exten

        xdef    net

        xref    ut_gxin1                 ; get one integer

        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_sys'

net
        jsr     ut_gxin1                 ; get node
        bne.s   net_rts
        moveq   #sms.info,d0
        trap    #do.sms2
        move.b  1(a6,a1.l),sys_nnnr(a0)
net_rts
        rts
        end
